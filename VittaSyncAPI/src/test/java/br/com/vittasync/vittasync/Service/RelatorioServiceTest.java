package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.RelatorioCSVDTO;
import br.com.vittasync.vittasync.DTO.RelatorioPDFDTO;
import br.com.vittasync.vittasync.DTO.RelatorioPreviewDTO;
import br.com.vittasync.vittasync.DTO.RelatorioResumoDTO;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.RelatorioLog;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.RelatorioLogRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class RelatorioServiceTest {

    private SinaisVitaisRepository sinaisVitaisRepository;
    private HabitosRepository habitosRepository;
    private DiarioSintomasRepository diarioSintomasRepository;
    private RelatorioLogRepository relatorioLogRepository;
    private RelatorioService service;

    private Usuario paciente;

    @BeforeEach
    void setup() {
        sinaisVitaisRepository = mock(SinaisVitaisRepository.class);
        habitosRepository = mock(HabitosRepository.class);
        diarioSintomasRepository = mock(DiarioSintomasRepository.class);
        relatorioLogRepository = mock(RelatorioLogRepository.class);

        service = new RelatorioService(sinaisVitaisRepository, habitosRepository, diarioSintomasRepository, relatorioLogRepository);

        paciente = new Usuario();
        paciente.setId(1);
        paciente.setNome("Paciente Teste");
        paciente.setCpf("123");
        paciente.setPesoInicial(70.0);
        paciente.setAltura(1.75);
        paciente.setDataNascimento(LocalDate.of(1990, 1, 1));
    }

    private SinaisVitais sinais(int id, LocalDateTime data, Integer fc) {
        SinaisVitais s = new SinaisVitais();
        s.setId(id);
        s.setDataRegistro(data);
        s.setPeso(70.0);
        s.setFcBpm(fc);
        s.setFrRpm(16);
        s.setPaSistolica(120);
        s.setPaDiastolica(80);
        s.setTempCelcius(36.5);
        s.setSpo2Porcento(98);
        return s;
    }

    private Habitos habito(int id, LocalDate referencia, Integer sono, Integer exercicio) {
        Habitos h = new Habitos();
        h.setId(id);
        h.setDataRegistro(LocalDateTime.now());
        h.setDataReferencia(referencia);
        h.setHorasSono(sono);
        h.setMinutosExercicio(exercicio);
        h.setIndiceRepouso(1.0);
        h.setRepouso(false);
        h.setCanal("app");
        return h;
    }

    private DiarioSintomas sintoma(int id, String nome) {
        DiarioSintomas s = new DiarioSintomas();
        s.setId(id);
        s.setDataRegistro(LocalDateTime.now());
        s.setDataReferencia(LocalDate.now());
        s.setSintoma(nome);
        s.setIntensidadeDor(5);
        return s;
    }

    private void mockarRegistros() {
        LocalDateTime hoje = LocalDateTime.now();
        when(sinaisVitaisRepository.buscarParaRelatorio(eq(1), any(), any())).thenReturn(List.of(
                sinais(1, hoje.minusDays(3), 70),
                sinais(2, hoje.minusDays(3), 80),
                sinais(3, hoje, 90),
                sinais(4, null, null)));
        when(habitosRepository.buscarParaRelatorio(eq(1), any(), any())).thenReturn(List.of(
                habito(1, LocalDate.now(), 8, 30),
                habito(2, LocalDate.now(), 6, 15),
                habito(3, LocalDate.now().minusDays(1), null, null),
                habito(4, null, 7, 10)));
        when(diarioSintomasRepository.buscarParaRelatorio(eq(1), any(), any())).thenReturn(List.of(
                sintoma(1, "Dor de cabeça"), sintoma(2, "=SOMA(A1)")));
    }

    @Test
    void testGerarPreviewComTodasAsCategorias() {
        mockarRegistros();
        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fim = LocalDate.now();

        RelatorioPreviewDTO preview = service.gerarPreview(paciente, List.of("SINAIS", "HABITOS", "SINTOMAS"), inicio, fim);

        assertThat(preview.getPaciente().getNome()).isEqualTo("Paciente Teste");
        assertThat(preview.getSinaisVitais()).hasSize(4);
        assertThat(preview.getHabitos()).hasSize(4);
        assertThat(preview.getSintomas()).hasSize(2);
        assertThat(preview.getDataInicio()).isEqualTo(inicio);
        assertThat(preview.getDataFim()).isEqualTo(fim);
        assertThat(preview.getDataEmissao()).isNotNull();
        assertThat(preview.isSemRegistros()).isFalse();

        RelatorioResumoDTO.Indicador fc = preview.getResumo().sinaisVitais().get(0);
        assertThat(fc.chave()).isEqualTo("fcBpm");
        assertThat(fc.media()).isEqualTo(80.0);
        assertThat(fc.minimo()).isEqualTo(70.0);
        assertThat(fc.maximo()).isEqualTo(90.0);
        assertThat(fc.quantidade()).isEqualTo(3);
        assertThat(fc.evolucaoDiaria()).hasSize(2);

        RelatorioResumoDTO.Habitos habitos = preview.getResumo().habitos();
        assertThat(habitos.mediaHorasSono()).isEqualTo(7.0);
        assertThat(habitos.diasComSono()).isEqualTo(1);
        assertThat(habitos.totalMinutosExercicio()).isEqualTo(45L);
        assertThat(habitos.diasComExercicio()).isEqualTo(1);

        verify(sinaisVitaisRepository).buscarParaRelatorio(1, inicio.atStartOfDay(), fim.plusDays(1).atStartOfDay());
    }

    @Test
    void testGerarPreviewSomenteSintomasSemPeriodo() {
        mockarRegistros();

        RelatorioPreviewDTO preview = service.gerarPreview(paciente, List.of("SINTOMAS"), null, null);

        assertThat(preview.getSinaisVitais()).isEmpty();
        assertThat(preview.getHabitos()).isEmpty();
        assertThat(preview.getSintomas()).hasSize(2);
        assertThat(preview.getResumo().sinaisVitais()).isEmpty();
        assertThat(preview.getResumo().habitos()).isNull();
        verifyNoInteractions(sinaisVitaisRepository, habitosRepository);
    }

    @Test
    void testGerarPreviewDataFinalMaximaBuscaSemLimite() {
        when(sinaisVitaisRepository.buscarParaRelatorio(eq(1), any(), any())).thenReturn(List.of());
        when(habitosRepository.buscarParaRelatorio(eq(1), any(), any())).thenReturn(List.of());

        RelatorioPreviewDTO preview = service.gerarPreview(paciente, List.of("SINAIS", "HABITOS"),
                null, LocalDate.of(9999, 12, 31));

        assertThat(preview.isSemRegistros()).isTrue();
        assertThat(preview.getResumo().sinaisVitais().get(0).media()).isNull();
        assertThat(preview.getResumo().habitos().mediaHorasSono()).isNull();
        assertThat(preview.getResumo().habitos().totalMinutosExercicio()).isNull();
        verify(sinaisVitaisRepository).buscarParaRelatorio(1, null, null);
    }

    @Test
    void testGerarPDF() {
        mockarRegistros();
        RelatorioPreviewDTO preview = service.gerarPreview(paciente, List.of("SINAIS", "HABITOS", "SINTOMAS"),
                LocalDate.now().minusDays(7), LocalDate.now());

        RelatorioPDFDTO pdf = service.gerarPDF(preview);

        assertThat(pdf.getArquivo()).isNotEmpty();
        assertThat(new String(pdf.getArquivo(), 0, 4, StandardCharsets.ISO_8859_1)).isEqualTo("%PDF");
    }

    @Test
    void testGerarPDFComErro() {
        RelatorioPreviewDTO preview = service.gerarPreview(paciente, List.of(), null, null);
        preview.setPaciente(null);

        assertThrows(RuntimeException.class, () -> service.gerarPDF(preview));
    }

    @Test
    void testGerarCSV() {
        mockarRegistros();
        RelatorioPreviewDTO preview = service.gerarPreview(paciente, List.of("SINAIS", "HABITOS", "SINTOMAS"), null, null);

        RelatorioCSVDTO csv = service.gerarCSV(preview);

        String conteudo = new String(csv.getArquivo(), StandardCharsets.UTF_8);
        assertThat(conteudo).startsWith("﻿\"categoria\"");
        assertThat(conteudo).contains("\"SINAIS\"", "\"HABITOS\"", "\"SINTOMAS\"", "\"'=SOMA(A1)\"");
    }

    @Test
    void testRegistrarExportacao() {
        Usuario usuario = new Usuario();
        usuario.setId(2);

        service.registrarExportacao(paciente, usuario, "PDF", List.of("SINAIS", "HABITOS"),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        ArgumentCaptor<RelatorioLog> captor = ArgumentCaptor.forClass(RelatorioLog.class);
        verify(relatorioLogRepository).save(captor.capture());
        assertThat(captor.getValue().getPaciente()).isEqualTo(paciente);
        assertThat(captor.getValue().getUsuario()).isEqualTo(usuario);
        assertThat(captor.getValue().getFormato()).isEqualTo("PDF");
        assertThat(captor.getValue().getCategorias()).isEqualTo("SINAIS,HABITOS");
        assertThat(captor.getValue().getDataExportacao()).isNotNull();
    }

    @Test
    void testConsultarLogs() {
        RelatorioLog log = new RelatorioLog();
        when(relatorioLogRepository.findByPacienteIdOrderByDataExportacaoDesc(1)).thenReturn(List.of(log));

        assertThat(service.consultarLogs(1)).containsExactly(log);
    }
}
