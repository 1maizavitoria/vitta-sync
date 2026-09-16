package br.com.vittasync.vittasync.Util;


import br.com.vittasync.vittasync.DTO.RelatorioPreviewDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.ImageDataFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;


public class RelatorioPDFGenerator {

    public byte[] gerarRelatorio(RelatorioPreviewDTO preview) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Table header = new Table(4);
        header.addCell("Paciente");
        header.addCell(preview.getPaciente().getNome());
        header.addCell("Nascimento");
        header.addCell(preview.getPaciente().getDataNascimento().toString());
        document.add(header);

        Table sinaisTable = new Table(5);
        sinaisTable.addCell("Indicador");
        sinaisTable.addCell("Média");
        sinaisTable.addCell("Mínimo");
        sinaisTable.addCell("Máximo");
        sinaisTable.addCell("Último");

        document.add(new Paragraph("Resumo dos sinais vitais"));
        document.add(sinaisTable);

        Table habitosTable = new Table(2);
        habitosTable.addCell("Sono médio");
        habitosTable.addCell("7,2 horas/noite"); // calcular a partir do preview
        habitosTable.addCell("Exercício médio");
        habitosTable.addCell("32 minutos/dia"); // idem
        document.add(new Paragraph("Hábitos"));
        document.add(habitosTable);

        Table sintomasTable = new Table(2);
        sintomasTable.addCell("Registros de sintomas no período");
        sintomasTable.addCell("4"); // calcular
        sintomasTable.addCell("Maior intensidade registrada");
        sintomasTable.addCell("6/10"); // calcular
        document.add(new Paragraph("Resumo de sintomas"));
        document.add(sintomasTable);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        preview.getSinaisVitais().forEach(s ->
                dataset.addValue(s.getFcBpm(), "FC", s.getDataHora().toLocalDate().toString())
        );
        JFreeChart chart = ChartFactory.createLineChart(
                "Frequência cardíaca", "Data", "bpm", dataset);
        ByteArrayOutputStream chartBaos = new ByteArrayOutputStream();
        ImageIO.write(chart.createBufferedImage(400, 300), "PNG", chartBaos);
        Image chartImage = new Image(ImageDataFactory.create(chartBaos.toByteArray()));
        document.add(new Paragraph("Evolução dos sinais vitais"));
        document.add(chartImage);

        document.add(new Paragraph("Observação: este relatório consolida registros de acompanhamento do VittaSync e não substitui avaliação médica."));

        document.close();
        return baos.toByteArray();
    }
}
