package br.com.vittasync.vittasync.Util;

import br.com.vittasync.vittasync.DTO.RelatorioPreviewDTO;
import br.com.vittasync.vittasync.DTO.RelatorioResumoDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.io.image.ImageDataFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RelatorioPDFGenerator {
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter EMISSAO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    public byte[] gerarRelatorio(RelatorioPreviewDTO preview) throws Exception {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        try (Document documento = new Document(new PdfDocument(new PdfWriter(saida)))) {
            documento.setFontSize(10);
            documento.add(new Paragraph("VITTASYNC - RELATÓRIO DE ACOMPANHAMENTO").setBold().setFontSize(16));
            documento.add(new Paragraph("Paciente: " + texto(preview.getPaciente().getNome())
                    + "\nNascimento: " + data(preview.getPaciente().getDataNascimento())
                    + "\nPeríodo: " + periodo(preview)
                    + "\nEmitido em: " + preview.getDataEmissao().format(EMISSAO)));

            if (preview.getCategorias().contains("SINAIS")) adicionarSinais(documento, preview);
            if (preview.getCategorias().contains("SINTOMAS")) adicionarSintomas(documento, preview);
            if (preview.getCategorias().contains("HABITOS")) adicionarHabitos(documento, preview);

            documento.add(new Paragraph("Valores ausentes não são considerados como zero. "
                    + "Este relatório consolida registros do VittaSync e não substitui avaliação médica.").setFontSize(8));

            if (preview.getCategorias().contains("SINAIS") && preview.getResumo().sinaisVitais().stream()
                    .anyMatch(i -> !i.evolucaoDiaria().isEmpty())) {
                documento.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                titulo(documento, "Evolução dos sinais vitais");
                documento.add(new Paragraph("Médias diárias. Dias sem medições não têm valor atribuído.").setFontSize(9));
                for (RelatorioResumoDTO.Indicador indicador : preview.getResumo().sinaisVitais()) {
                    if (!indicador.evolucaoDiaria().isEmpty()) adicionarGrafico(documento, indicador);
                }
            }
        }
        return saida.toByteArray();
    }

    private void adicionarSinais(Document documento, RelatorioPreviewDTO preview) {
        titulo(documento, "Resumo dos sinais vitais");
        Table tabela = new Table(new float[]{3, 1, 1, 1, 1}).useAllAvailableWidth();
        for (String coluna : new String[]{"Indicador", "Média", "Mínimo", "Máximo", "Medições"}) {
            tabela.addHeaderCell(coluna);
        }
        for (RelatorioResumoDTO.Indicador indicador : preview.getResumo().sinaisVitais()) {
            tabela.addCell(indicador.nome() + " (" + indicador.unidade() + ")");
            tabela.addCell(numero(indicador.media()));
            tabela.addCell(numero(indicador.minimo()));
            tabela.addCell(numero(indicador.maximo()));
            tabela.addCell(Long.toString(indicador.quantidade()));
        }
        documento.add(tabela);
        documento.add(new Paragraph("Médias calculadas por medição disponível. '-' indica ausência de valor.").setFontSize(8));
    }

    private void adicionarSintomas(Document documento, RelatorioPreviewDTO preview) {
        titulo(documento, "Sintomas");
        if (preview.getSintomas().isEmpty()) {
            documento.add(new Paragraph("Sem registros de sintomas no período."));
            return;
        }
        Table tabela = new Table(new float[]{1, 3, 1}).useAllAvailableWidth();
        tabela.addHeaderCell("Ocorrência");
        tabela.addHeaderCell("Sintoma");
        tabela.addHeaderCell("Dor (1-10)");
        preview.getSintomas().forEach(s -> {
            tabela.addCell(data(s.getDataReferencia()));
            tabela.addCell(texto(s.getSintoma()));
            tabela.addCell(s.getIntensidadeDor() == null ? "-" : s.getIntensidadeDor().toString());
        });
        documento.add(tabela);
    }

    private void adicionarHabitos(Document documento, RelatorioPreviewDTO preview) {
        titulo(documento, "Hábitos");
        RelatorioResumoDTO.Habitos habitos = preview.getResumo().habitos();
        Table tabela = new Table(2).useAllAvailableWidth();
        tabela.addCell("Sono médio (horas/noite)");
        tabela.addCell(numero(habitos.mediaHorasSono()));
        tabela.addCell("Dias com registro de sono");
        tabela.addCell(Long.toString(habitos.diasComSono()));
        tabela.addCell("Atividade física total (minutos)");
        tabela.addCell(habitos.totalMinutosExercicio() == null ? "-" : habitos.totalMinutosExercicio().toString());
        tabela.addCell("Dias com registro de atividade física");
        tabela.addCell(Long.toString(habitos.diasComExercicio()));
        documento.add(tabela);
        documento.add(new Paragraph("Sono: média das médias diárias registradas. "
                + "Atividade física: soma dos minutos registrados. Resumo por data de referência.").setFontSize(8));
    }

    private void adicionarGrafico(Document documento, RelatorioResumoDTO.Indicador indicador) throws Exception {
        TimeSeries serie = new TimeSeries(indicador.nome());
        LocalDate anterior = null;
        for (RelatorioResumoDTO.PontoDiario ponto : indicador.evolucaoDiaria()) {
            // Interrompe a linha nos intervalos sem registro, sem inventar medições.
            if (anterior != null && ponto.data().isAfter(anterior.plusDays(1))) {
                serie.add(dia(anterior.plusDays(1)), (Number) null);
            }
            serie.add(dia(ponto.data()), ponto.media());
            anterior = ponto.data();
        }
        JFreeChart grafico = ChartFactory.createTimeSeriesChart(indicador.nome(), "Data",
                indicador.unidade(), new TimeSeriesCollection(serie), false, false, false);
        grafico.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ByteArrayOutputStream imagem = new ByteArrayOutputStream();
        ImageIO.write(grafico.createBufferedImage(1000, 380), "PNG", imagem);
        documento.add(new Image(ImageDataFactory.create(imagem.toByteArray())).setWidth(500).setHeight(190));
    }

    private FixedMillisecond dia(LocalDate data) {
        return new FixedMillisecond(data.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
    private void titulo(Document documento, String titulo) {
        documento.add(new Paragraph(titulo).setBold().setFontSize(12).setKeepWithNext(true));
    }
    private String numero(Double valor) { return valor == null ? "-" : String.format(PT_BR, "%.1f", valor); }
    private String texto(String valor) { return valor == null || valor.isBlank() ? "Sem registro" : valor; }
    private String data(LocalDate valor) { return valor == null ? "Sem registro" : valor.format(DATA); }
    private String periodo(RelatorioPreviewDTO preview) {
        if (preview.getDataInicio() == null && preview.getDataFim() == null) return "Todos os registros";
        if (preview.getDataInicio() == null) return "Até " + data(preview.getDataFim());
        if (preview.getDataFim() == null) return "A partir de " + data(preview.getDataInicio());
        return data(preview.getDataInicio()) + " a " + data(preview.getDataFim());
    }
}
