package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.DashboardCategoriaDTO;
import br.com.vittasync.vittasync.DTO.DashboardResponseDTO;
import br.com.vittasync.vittasync.DTO.LinhaBaseDTO;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.LinhaBase;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.LinhaBaseRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class DashboardServiceTest {

    private SinaisVitaisRepository sinaisVitaisRepository;
    private HabitosRepository habitosRepository;
    private EstabilidadeClinicaService estabilidadeClinicaService;
    private LinhaBaseRepository linhaBaseRepository;
    private DashboardService service;

    private Usuario paciente;
    private SinaisVitais sinais;
    private Habitos habito;

    @BeforeEach
    void setup() {
        sinaisVitaisRepository = mock(SinaisVitaisRepository.class);
        habitosRepository = mock(HabitosRepository.class);
        estabilidadeClinicaService = mock(EstabilidadeClinicaService.class);
        linhaBaseRepository = mock(LinhaBaseRepository.class);

        LinhaBaseService linhaBaseService = new LinhaBaseService(linhaBaseRepository, sinaisVitaisRepository,
                mock(UsuarioRepository.class));

        service = new DashboardService(sinaisVitaisRepository, habitosRepository, estabilidadeClinicaService,
                linhaBaseService, linhaBaseRepository);

        paciente = new Usuario();
        paciente.setId(1);
        paciente.setCpf("123");
        paciente.setNome("Paciente Teste");

        sinais = new SinaisVitais();
        sinais.setPeso(70.0);
        sinais.setFcBpm(90);
        sinais.setFrRpm(16);
        sinais.setPaSistolica(120);
        sinais.setPaDiastolica(80);
        sinais.setTempCelcius(36.5);
        sinais.setSpo2Porcento(98);
        sinais.setDataRegistro(LocalDateTime.now());

        habito = new Habitos();
        habito.setHorasSono(8);
        habito.setMinutosExercicio(null);
        habito.setDataReferencia(LocalDate.now());

        when(sinaisVitaisRepository.findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(eq(1), any(), any()))
                .thenReturn(List.of(sinais));
        when(habitosRepository.findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(eq(1), any(), any()))
                .thenReturn(List.of(habito));
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(List.of(sinais));
        when(estabilidadeClinicaService.calcularIndices(eq(1), anyList(), anyList())).thenReturn(List.of());
    }

    private LinhaBase base(String sinal, Double inferior, Double superior, LocalDateTime formacao) {
        LinhaBase base = new LinhaBase();
        base.setSinal(sinal);
        base.setMedia(75.0);
        base.setDesvioPadrao(inferior == null ? 0.0 : 2.0);
        base.setLimiteInferior(inferior);
        base.setLimiteSuperior(superior);
        base.setDataInicio(LocalDate.now().minusDays(20));
        base.setDataFim(LocalDate.now().minusDays(7));
        base.setDataFormacao(formacao);
        return base;
    }

    private LinhaBaseDTO linha(DashboardResponseDTO resposta, String sinal) {
        return resposta.getLinhasBase().stream().filter(l -> sinal.equals(l.getSinal())).findFirst().orElseThrow();
    }

    @Test
    void testConsultarTodasAsCategoriasComPeriodoPadrao() {
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of());

        DashboardResponseDTO resposta = service.consultar(paciente, null, null, null);

        assertThat(resposta.getPacienteCpf()).isEqualTo("123");
        assertThat(resposta.getPacienteNome()).isEqualTo("Paciente Teste");
        assertThat(resposta.getFim()).isEqualTo(LocalDate.now());
        assertThat(resposta.getInicio()).isEqualTo(LocalDate.now().minusDays(6));
        assertThat(resposta.getCategorias()).extracting(DashboardCategoriaDTO::getCodigo).containsExactly(
                "pressao", "frequencia_cardiaca", "frequencia_respiratoria", "temperatura",
                "saturacao", "peso", "sono", "exercicio");

        DashboardCategoriaDTO pressao = resposta.getCategorias().get(0);
        assertThat(pressao.getSeries()).hasSize(2);
        assertThat(pressao.getSeries().get(0).getPontos().get(0).getValor()).isEqualTo(120);

        DashboardCategoriaDTO sono = resposta.getCategorias().get(6);
        assertThat(sono.getSeries().get(0).getPontos()).hasSize(1);
        DashboardCategoriaDTO exercicio = resposta.getCategorias().get(7);
        assertThat(exercicio.getSeries().get(0).getPontos()).isEmpty();

        assertThat(resposta.getLinhasBase()).hasSize(7);
        LinhaBaseDTO peso = linha(resposta, "peso");
        assertThat(peso.getSituacao()).isEqualTo("em_formacao");
        assertThat(peso.getDiasRegistrados()).isEqualTo(1);
        assertThat(peso.getUltimoValor()).isEqualTo(70.0);
        assertThat(peso.getComparacao()).isNull();
    }

    @Test
    void testConsultarCategoriasFiltradas() {
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of());

        DashboardResponseDTO resposta = service.consultar(paciente,
                LocalDate.now().minusDays(2), LocalDate.now(), " peso , SONO ");

        assertThat(resposta.getCategorias()).extracting(DashboardCategoriaDTO::getCodigo)
                .containsExactly("peso", "sono");
    }

    @Test
    void testConsultarCategoriaInvalida() {
        assertThrows(DadosInvalidosException.class, () -> service.consultar(paciente, null, null, "peso,outra"));
        assertThrows(DadosInvalidosException.class, () -> service.consultar(paciente, null, null, "peso,,sono"));
    }

    @Test
    void testConsultarDataInicialPosteriorAFinal() {
        assertThrows(DadosInvalidosException.class,
                () -> service.consultar(paciente, LocalDate.now(), LocalDate.now().minusDays(1), null));
    }

    @Test
    void testConsultarLinhaBaseFormadaComMedicaoNova() {
        LocalDateTime formacao = LocalDateTime.now().minusDays(1);
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of(
                base("frequencia_cardiaca", 70.0, 80.0, formacao),
                base("saturacao", null, null, formacao),
                base("peso", 60.0, 80.0, LocalDateTime.now().plusDays(1))
        ));

        DashboardResponseDTO resposta = service.consultar(paciente, null, null, "peso");

        LinhaBaseDTO fc = linha(resposta, "frequencia_cardiaca");
        assertThat(fc.getSituacao()).isEqualTo("formada");
        assertThat(fc.getDiasRegistrados()).isEqualTo(14);
        assertThat(fc.getComparacao()).isEqualTo("acima");
        assertThat(fc.getMedia()).isEqualTo(75.0);

        LinhaBaseDTO saturacao = linha(resposta, "saturacao");
        assertThat(saturacao.getSituacao()).isEqualTo("sem_variacao");
        assertThat(saturacao.getComparacao()).isNull();

        LinhaBaseDTO peso = linha(resposta, "peso");
        assertThat(peso.getSituacao()).isEqualTo("formada");
        assertThat(peso.getComparacao()).isNull();
    }

    @Test
    void testConsultarLinhaBaseSemHistorico() {
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(List.of());
        when(linhaBaseRepository.findByPacienteId(1)).thenReturn(List.of(
                base("frequencia_cardiaca", 70.0, 80.0, LocalDateTime.now())));

        DashboardResponseDTO resposta = service.consultar(paciente, null, null, null);

        LinhaBaseDTO fc = linha(resposta, "frequencia_cardiaca");
        assertThat(fc.getUltimoValor()).isNull();
        assertThat(fc.getDataUltimoValor()).isNull();
        assertThat(fc.getComparacao()).isNull();
        assertThat(linha(resposta, "peso").getDiasRegistrados()).isEqualTo(0);
    }
}
