package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.LinhaBase;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.ContatoEmergenciaRepository;
import br.com.vittasync.vittasync.Repository.LinhaBaseRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class LinhaBaseAlertaServiceTest {

    private LinhaBaseRepository linhaBaseRepository;
    private EventoPacienteService eventoPacienteService;
    private ContatoEmergenciaRepository contatoEmergenciaRepository;
    private NotificacaoService notificacaoService;
    private LinhaBaseAlertaService service;

    private Usuario paciente;
    private LocalDateTime formacao;

    @BeforeEach
    void setup() {
        linhaBaseRepository = mock(LinhaBaseRepository.class);
        eventoPacienteService = mock(EventoPacienteService.class);
        contatoEmergenciaRepository = mock(ContatoEmergenciaRepository.class);
        notificacaoService = mock(NotificacaoService.class);

        LinhaBaseService linhaBaseService = new LinhaBaseService(linhaBaseRepository,
                mock(SinaisVitaisRepository.class), mock(UsuarioRepository.class));

        service = new LinhaBaseAlertaService(linhaBaseRepository, linhaBaseService,
                eventoPacienteService, contatoEmergenciaRepository, notificacaoService);

        paciente = new Usuario();
        paciente.setId(1);
        paciente.setNome("Paciente Teste");

        formacao = LocalDateTime.now().minusDays(1);
    }

    private LinhaBase base(String sinal, double inferior, double superior) {
        LinhaBase base = new LinhaBase();
        base.setPacienteId(1);
        base.setSinal(sinal);
        base.setLimiteInferior(inferior);
        base.setLimiteSuperior(superior);
        base.setDataFormacao(formacao);
        return base;
    }

    private SinaisVitais medicao(LocalDateTime data) {
        SinaisVitais sinais = new SinaisVitais();
        sinais.setId(10);
        sinais.setPaciente(paciente);
        sinais.setDataRegistro(data);
        sinais.setPeso(50.0);
        sinais.setFcBpm(120);
        sinais.setFrRpm(16);
        sinais.setPaSistolica(150);
        sinais.setPaDiastolica(50);
        sinais.setTempCelcius(39.0);
        sinais.setSpo2Porcento(85);
        return sinais;
    }

    private ContatoEmergencia contato(int id) {
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(id);
        contato.setNome("Contato " + id);
        contato.setEmail("contato" + id + "@teste.com");
        contato.setTelefone("41999999999");
        contato.setCanalEmail(true);
        contato.setCanalSms(false);
        return contato;
    }

    @Test
    void testNotificaDesviosDeTodosOsSinais() {
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of(
                base("peso", 60, 80),
                base("frequencia_cardiaca", 60, 90),
                base("frequencia_respiratoria", 12, 20),
                base("pressao_sistolica", 110, 130),
                base("pressao_diastolica", 70, 90),
                base("temperatura", 36, 37.5),
                base("saturacao", 94, 100)
        ));
        when(contatoEmergenciaRepository.findByPacienteIdOrderByDataRegistroAsc(1))
                .thenReturn(List.of(contato(1), contato(2)));

        service.notificarDesvios(medicao(LocalDateTime.now()), 99, Set.of(2));

        ArgumentCaptor<String> detalhes = ArgumentCaptor.forClass(String.class);
        verify(eventoPacienteService).criarEvento(eq(1), eq(99), eq(EventoTipos.DESVIO_LINHA_BASE),
                eq("Medição fora do padrão individual"), detalhes.capture(), anyString(), eq(EventoPrioridades.ALTA));

        assertThat(detalhes.getValue()).contains("Peso", "Frequência cardíaca", "Pressão sistólica",
                "Pressão diastólica", "Temperatura", "Saturação", "abaixo", "acima");
        assertThat(detalhes.getValue()).doesNotContain("Frequência respiratória");

        verify(notificacaoService).enviarDesvioLinhaBase(eq(1), eq("Contato 1"), eq("contato1@teste.com"),
                eq("41999999999"), eq(true), eq(false), eq("Paciente Teste"), anyString());
        verify(notificacaoService, never()).enviarDesvioLinhaBase(eq(2), any(), any(), any(),
                anyBoolean(), anyBoolean(), any(), any());
    }

    @Test
    void testSemDesvioNaoNotifica() {
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of(base("frequencia_respiratoria", 12, 20)));

        service.notificarDesvios(medicao(LocalDateTime.now()), 99, Set.of());

        verifyNoInteractions(eventoPacienteService, notificacaoService);
    }

    @Test
    void testMedicaoAnteriorAFormacaoNaoEComparada() {
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of(base("frequencia_cardiaca", 60, 90)));

        service.notificarDesvios(medicao(formacao.minusHours(1)), 99, Set.of());
        service.notificarDesvios(medicao(null), 99, Set.of());

        verifyNoInteractions(eventoPacienteService, notificacaoService);
    }

    @Test
    void testBaseSemDataDeFormacaoEIgnorada() {
        LinhaBase base = base("frequencia_cardiaca", 60, 90);
        base.setDataFormacao(null);
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of(base));

        service.notificarDesvios(medicao(LocalDateTime.now()), 99, Set.of());

        verifyNoInteractions(eventoPacienteService, notificacaoService);
    }

    @Test
    void testSinalDesconhecidoNaoGeraDesvio() {
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of(base("outro", 0, 1)));

        service.notificarDesvios(medicao(LocalDateTime.now()), 99, Set.of());

        verifyNoInteractions(eventoPacienteService);
    }
}
