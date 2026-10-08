package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.EstabilidadeClinicaDTO;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.EstabilidadeClinica;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Repository.ContatoEmergenciaRepository;
import br.com.vittasync.vittasync.Repository.EstabilidadeClinicaRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class EstabilidadeClinicaServiceTest {

    private EstabilidadeClinicaRepository estabilidadeClinicaRepository;
    private ContatoEmergenciaRepository contatoEmergenciaRepository;
    private NotificacaoService notificacaoService;
    private SinaisVitaisRepository sinaisVitaisRepository;
    private HabitosRepository habitosRepository;
    private EstabilidadeClinicaService service;

    @BeforeEach
    void setup() {
        estabilidadeClinicaRepository = mock(EstabilidadeClinicaRepository.class);
        contatoEmergenciaRepository = mock(ContatoEmergenciaRepository.class);
        notificacaoService = mock(NotificacaoService.class);
        sinaisVitaisRepository = mock(SinaisVitaisRepository.class);
        habitosRepository = mock(HabitosRepository.class);

        service = new EstabilidadeClinicaService(estabilidadeClinicaRepository, contatoEmergenciaRepository,
                notificacaoService, sinaisVitaisRepository, habitosRepository);
    }

    private SinaisVitais sinais(int fc, int fr, int sistolica, int diastolica, double temp, int spo2) {
        SinaisVitais s = new SinaisVitais();
        s.setFcBpm(fc);
        s.setFrRpm(fr);
        s.setPaSistolica(sistolica);
        s.setPaDiastolica(diastolica);
        s.setTempCelcius(temp);
        s.setSpo2Porcento(spo2);
        s.setPeso(70.0);
        return s;
    }

    private List<SinaisVitais> repetir(SinaisVitais s) {
        return List.of(s, s, s);
    }

    private Habitos habito(Integer sono, Integer exercicio) {
        Habitos h = new Habitos();
        h.setHorasSono(sono);
        h.setMinutosExercicio(exercicio);
        return h;
    }

    private EstabilidadeClinicaDTO indice(List<EstabilidadeClinicaDTO> indices, String tipo) {
        return indices.stream().filter(i -> tipo.equals(i.getTipo())).findFirst().orElseThrow();
    }

    @Test
    void testCalcularIndicesSaudavel() {
        List<Habitos> habitos = List.of(habito(8, 60), habito(8, 70), habito(8, 80));

        List<EstabilidadeClinicaDTO> indices = service.calcularIndices(1,
                repetir(sinais(80, 16, 110, 70, 36.5, 98)), habitos);

        assertThat(indices).hasSize(9);
        assertThat(indice(indices, "fc_bpm").getCategoria()).isEqualTo("saudavel");
        assertThat(indice(indices, "fr_rpm").getIndice()).isEqualTo(9);
        assertThat(indice(indices, "pressao").getIndice()).isEqualTo(9);
        assertThat(indice(indices, "temp_celcius").getIndice()).isEqualTo(9);
        assertThat(indice(indices, "spo2").getIndice()).isEqualTo(9);
        assertThat(indice(indices, "peso").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "sono").getIndice()).isEqualTo(9);
        assertThat(indice(indices, "exercicio").getIndice()).isEqualTo(9);
        assertThat(indice(indices, "geral").getCategoria()).isEqualTo("saudavel");
    }

    @Test
    void testCalcularIndicesCritico() {
        List<Habitos> habitos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            habitos.add(habito(4, 0));
        }

        List<EstabilidadeClinicaDTO> indices = service.calcularIndices(1,
                repetir(sinais(130, 35, 150, 110, 40.0, 85)), habitos);

        assertThat(indice(indices, "fc_bpm").getIndice()).isEqualTo(3);
        assertThat(indice(indices, "fr_rpm").getIndice()).isEqualTo(3);
        assertThat(indice(indices, "pressao").getIndice()).isEqualTo(3);
        assertThat(indice(indices, "temp_celcius").getIndice()).isEqualTo(3);
        assertThat(indice(indices, "spo2").getIndice()).isEqualTo(3);
        assertThat(indice(indices, "sono").getCategoria()).isEqualTo("critico");
        assertThat(indice(indices, "exercicio").getCategoria()).isEqualTo("critico");
        assertThat(indice(indices, "geral").getCategoria()).isEqualTo("critico");
    }

    @Test
    void testCalcularIndicesModerado() {
        List<Habitos> habitos = List.of(habito(6, 20), habito(6, 20), habito(6, 20));

        List<EstabilidadeClinicaDTO> indices = service.calcularIndices(1,
                repetir(sinais(110, 25, 130, 90, 38.0, 92)), habitos);

        assertThat(indice(indices, "fc_bpm").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "fr_rpm").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "pressao").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "temp_celcius").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "spo2").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "sono").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "exercicio").getIndice()).isEqualTo(6);
        assertThat(indice(indices, "geral").getCategoria()).isEqualTo("moderado");
    }

    @Test
    void testCalcularIndicesSemRegistrosSuficientes() {
        List<EstabilidadeClinicaDTO> indices = service.calcularIndices(1,
                List.of(sinais(80, 16, 110, 70, 36.5, 98)), List.of(habito(8, null)));

        assertThat(indice(indices, "fc_bpm").getIndice()).isNull();
        assertThat(indice(indices, "pressao").getCategoria()).isEqualTo("n/a");
        assertThat(indice(indices, "sono").getIndice()).isNull();
        assertThat(indice(indices, "exercicio").getIndice()).isNull();
        assertThat(indice(indices, "geral").getIndice()).isNull();
        assertThat(indice(indices, "geral").getCategoria()).isEqualTo("n/a");
    }

    @Test
    void testConsultarIndicesBuscaSinaisEHabitos() {
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1))
                .thenReturn(repetir(sinais(80, 16, 110, 70, 36.5, 98)));
        when(habitosRepository.findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(eq(1), any(), any()))
                .thenReturn(List.of());

        List<EstabilidadeClinicaDTO> indices = service.consultarIndices(1);

        assertThat(indice(indices, "geral").getCategoria()).isEqualTo("saudavel");
        verify(sinaisVitaisRepository).findByPacienteIdOrderByDataRegistroAsc(1);
    }

    @Test
    void testVerificarMudancaSalvaENotificaQuandoCategoriaMuda() {
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setReceberAlertaSinaisVitaisCritico(true);
        contato.setReceberAlertaHabitosCritico(true);
        contato.setReceberAlertaGeralCritico(true);

        List<Habitos> habitos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            habitos.add(habito(4, 0));
        }

        when(estabilidadeClinicaRepository.findUltimaCategoriaGeral(1)).thenReturn("saudavel");
        when(contatoEmergenciaRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(List.of(contato));

        service.verificarMudancaEstabilidade(1, repetir(sinais(130, 35, 150, 110, 40.0, 85)), habitos);

        ArgumentCaptor<EstabilidadeClinica> captor = ArgumentCaptor.forClass(EstabilidadeClinica.class);
        verify(estabilidadeClinicaRepository).save(captor.capture());
        assertThat(captor.getValue().getPacienteId()).isEqualTo(1);
        assertThat(captor.getValue().getTipo()).isEqualTo("geral");
        assertThat(captor.getValue().getCategoria()).isEqualTo("critico");

        ArgumentCaptor<String> mensagem = ArgumentCaptor.forClass(String.class);
        verify(notificacaoService).enviarAlertaEmergencia(eq(contato), mensagem.capture(), eq("critico"));
        assertThat(mensagem.getValue()).contains("fc_bpm", "sono", "Estabilidade geral");
    }

    @Test
    void testVerificarMudancaPrimeiroCalculoSemContatos() {
        when(estabilidadeClinicaRepository.findUltimaCategoriaGeral(1)).thenReturn(null);
        when(contatoEmergenciaRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(List.of());

        service.verificarMudancaEstabilidade(1, repetir(sinais(80, 16, 110, 70, 36.5, 98)), List.of());

        verify(estabilidadeClinicaRepository).save(any(EstabilidadeClinica.class));
        verifyNoInteractions(notificacaoService);
    }

    @Test
    void testVerificarMudancaNaoSalvaQuandoCategoriaIgual() {
        when(estabilidadeClinicaRepository.findUltimaCategoriaGeral(1)).thenReturn("saudavel");

        service.verificarMudancaEstabilidade(1, repetir(sinais(80, 16, 110, 70, 36.5, 98)), List.of());

        verify(estabilidadeClinicaRepository, never()).save(any());
        verifyNoInteractions(notificacaoService);
    }

    @Test
    void testTestarDisparoAlertaRespeitaFlagsDoContato() {
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setReceberAlertaSinaisVitaisSaudavel(true);
        contato.setReceberAlertaSinaisVitaisModerado(true);
        contato.setReceberAlertaHabitosSaudavel(true);
        contato.setReceberAlertaHabitosModerado(true);
        contato.setReceberAlertaGeralSaudavel(true);
        contato.setReceberAlertaGeralModerado(true);

        when(contatoEmergenciaRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(List.of(contato));

        LocalDateTime agora = LocalDateTime.now();
        List<EstabilidadeClinicaDTO> indices = List.of(
                new EstabilidadeClinicaDTO("fc_bpm", 9, "saudavel", 1.0, agora),
                new EstabilidadeClinicaDTO("spo2", 6, "moderado", 1.0, agora),
                new EstabilidadeClinicaDTO("peso", 6, "outra", 1.0, agora),
                new EstabilidadeClinicaDTO("sono", 9, "saudavel", 1.0, agora),
                new EstabilidadeClinicaDTO("exercicio", 6, "moderado", 1.0, agora),
                new EstabilidadeClinicaDTO("sono", 6, "outra", 1.0, agora),
                new EstabilidadeClinicaDTO("desconhecido", 9, "saudavel", 1.0, agora),
                new EstabilidadeClinicaDTO("fr_rpm", null, "n/a", 1.0, agora),
                new EstabilidadeClinicaDTO("geral", 7, "moderado", 1.0, agora),
                new EstabilidadeClinicaDTO("geral", 9, "saudavel", 1.0, agora),
                new EstabilidadeClinicaDTO("geral", 9, "outra", 1.0, agora)
        );

        service.testarDisparoAlerta(1, indices);

        verify(notificacaoService).enviarAlertaEmergencia(eq(contato), anyString(), eq("saudavel"));
        verify(notificacaoService).enviarAlertaEmergencia(eq(contato), anyString(), eq("moderado"));
        verify(notificacaoService, never()).enviarAlertaEmergencia(any(), anyString(), eq("outra"));
    }

    @Test
    void testTestarDisparoAlertaSemIndiceGeral() {
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setReceberAlertaSinaisVitaisCritico(false);
        contato.setReceberAlertaHabitosCritico(false);
        contato.setReceberAlertaGeralCritico(false);
        when(contatoEmergenciaRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(List.of(contato));

        service.testarDisparoAlerta(1, List.of(
                new EstabilidadeClinicaDTO("fc_bpm", 3, "critico", 1.0, LocalDateTime.now()),
                new EstabilidadeClinicaDTO("sono", 3, "critico", 1.0, LocalDateTime.now())));

        verifyNoInteractions(notificacaoService);
    }
}
