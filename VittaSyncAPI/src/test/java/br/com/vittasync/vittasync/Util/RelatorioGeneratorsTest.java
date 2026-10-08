package br.com.vittasync.vittasync.Util;


import br.com.vittasync.vittasync.DTO.LinhaTempoHabitosDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoSinaisVitaisDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoSintomasDTO;
import br.com.vittasync.vittasync.DTO.RelatorioPacienteResumoDTO;
import br.com.vittasync.vittasync.DTO.RelatorioPreviewDTO;
import br.com.vittasync.vittasync.DTO.RelatorioResumoDTO;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


class RelatorioGeneratorsTest {

    private RelatorioPreviewDTO preview(List<String> categorias, LocalDate inicio, LocalDate fim,
                                        List<LinhaTempoSintomasDTO> sintomas) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = LocalDate.now();

        RelatorioPacienteResumoDTO paciente = new RelatorioPacienteResumoDTO(" ", "123", 70.0, 1.75, null);
        LinhaTempoSinaisVitaisDTO sinal = new LinhaTempoSinaisVitaisDTO(1, agora, 70.0, 80, 16, 120, 80, 36.5, 98);
        LinhaTempoHabitosDTO habito = new LinhaTempoHabitosDTO(2, agora, 8, 30, 1.0, false, "\tapp", hoje);

        RelatorioResumoDTO.Indicador comEvolucao = new RelatorioResumoDTO.Indicador("fcBpm", "Frequência cardíaca", "bpm",
                80.0, 70.0, 90.0, 3, List.of(
                        new RelatorioResumoDTO.PontoDiario(hoje.minusDays(5), 70.0),
                        new RelatorioResumoDTO.PontoDiario(hoje.minusDays(4), 75.0),
                        new RelatorioResumoDTO.PontoDiario(hoje, 90.0)));
        RelatorioResumoDTO.Indicador semEvolucao = new RelatorioResumoDTO.Indicador("frRpm", "Frequência respiratória", "rpm",
                null, null, null, 0, List.of());

        RelatorioPreviewDTO preview = new RelatorioPreviewDTO(paciente, List.of(sinal), List.of(habito), sintomas);
        preview.setCategorias(categorias);
        preview.setDataInicio(inicio);
        preview.setDataFim(fim);
        preview.setDataEmissao(agora);
        preview.setResumo(new RelatorioResumoDTO(List.of(comEvolucao, semEvolucao),
                new RelatorioResumoDTO.Habitos(null, 0, null, 0)));
        return preview;
    }

    private boolean ehPdf(byte[] arquivo) {
        return new String(arquivo, 0, 4, StandardCharsets.ISO_8859_1).equals("%PDF");
    }

    @Test
    void testPdfComTodasAsCategoriasEPeriodoCompleto() throws Exception {
        List<LinhaTempoSintomasDTO> sintomas = List.of(
                new LinhaTempoSintomasDTO(3, LocalDateTime.now(), "Dor", 4, LocalDate.now()),
                new LinhaTempoSintomasDTO(4, LocalDateTime.now(), null, null, null));

        byte[] pdf = new RelatorioPDFGenerator().gerarRelatorio(preview(List.of("SINAIS", "SINTOMAS", "HABITOS"),
                LocalDate.now().minusDays(7), LocalDate.now(), sintomas));

        assertThat(ehPdf(pdf)).isTrue();
    }

    @Test
    void testPdfSemSintomasEPeriodosParciais() throws Exception {
        RelatorioPDFGenerator generator = new RelatorioPDFGenerator();

        assertThat(ehPdf(generator.gerarRelatorio(preview(List.of("SINTOMAS"), null, null, List.of())))).isTrue();
        assertThat(ehPdf(generator.gerarRelatorio(preview(List.of("HABITOS"), null, LocalDate.now(), List.of())))).isTrue();
        assertThat(ehPdf(generator.gerarRelatorio(preview(List.of("HABITOS"), LocalDate.now(), null, List.of())))).isTrue();
    }

    @Test
    void testPdfSinaisSemEvolucaoNaoGeraGraficos() throws Exception {
        RelatorioPreviewDTO preview = preview(List.of("SINAIS"), null, null, List.of());
        preview.setResumo(new RelatorioResumoDTO(List.of(new RelatorioResumoDTO.Indicador("frRpm",
                "Frequência respiratória", "rpm", 16.0, 16.0, 16.0, 1, List.of())), null));

        assertThat(ehPdf(new RelatorioPDFGenerator().gerarRelatorio(preview))).isTrue();
    }

    @Test
    void testCsvComTodasAsCategorias() {
        List<LinhaTempoSintomasDTO> sintomas = List.of(
                new LinhaTempoSintomasDTO(3, LocalDateTime.now(), "+ \"forte\"", 4, LocalDate.now()),
                new LinhaTempoSintomasDTO(4, LocalDateTime.now(), "", 2, LocalDate.now()));

        String csv = new String(new RelatorioCSVGenerator().gerarRelatorio(
                preview(List.of("SINAIS", "HABITOS", "SINTOMAS"), null, null, sintomas)), StandardCharsets.UTF_8);

        assertThat(csv).startsWith("﻿\"categoria\";\"id_registro\"");
        assertThat(csv).contains("\"SINAIS\";\"1\"", "\"HABITOS\";\"2\"", "\"'\tapp\"",
                "\"'+ \"\"forte\"\"\"", "\"SINTOMAS\";\"4\"");
        assertThat(csv.split("\r\n")).hasSize(5);
    }

    @Test
    void testCsvSemCategorias() {
        String csv = new String(new RelatorioCSVGenerator().gerarRelatorio(
                preview(List.of(), null, null, List.of())), StandardCharsets.UTF_8);

        assertThat(csv.split("\r\n")).hasSize(1);
    }
}
