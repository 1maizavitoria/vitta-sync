package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.LinhaBase;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.LinhaBaseRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class LinhaBaseServiceTest {

    private LinhaBaseRepository linhaBaseRepository;
    private SinaisVitaisRepository sinaisVitaisRepository;
    private UsuarioRepository usuarioRepository;
    private LinhaBaseService service;

    @BeforeEach
    void setup() {
        linhaBaseRepository = mock(LinhaBaseRepository.class);
        sinaisVitaisRepository = mock(SinaisVitaisRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);

        service = new LinhaBaseService(linhaBaseRepository, sinaisVitaisRepository, usuarioRepository);

        when(usuarioRepository.buscarPorIdComBloqueio(1)).thenReturn(Optional.of(new Usuario()));
    }

    private SinaisVitais registro(int diasAtras, int fc) {
        SinaisVitais sinais = new SinaisVitais();
        sinais.setPeso(70.0 + (fc % 3));
        sinais.setFcBpm(fc);
        sinais.setFrRpm(16);
        sinais.setPaSistolica(120 + (fc % 4));
        sinais.setPaDiastolica(80);
        sinais.setTempCelcius(36.5);
        sinais.setSpo2Porcento(98);
        sinais.setDataRegistro(LocalDate.now().minusDays(diasAtras).atTime(9, 0));
        return sinais;
    }

    private List<SinaisVitais> diasSeguidos(int quantidade) {
        List<SinaisVitais> lista = new ArrayList<>();
        for (int i = quantidade - 1; i >= 0; i--) {
            lista.add(registro(i, 70 + (i % 5)));
        }
        return lista;
    }

    @Test
    void testAtualizarLinhasBaseFormaReferenciaDosSeteSinais() {
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(diasSeguidos(14));
        when(linhaBaseRepository.existsByPacienteIdAndSinal(eq(1), anyString())).thenReturn(false);

        service.atualizarLinhasBase(1);

        ArgumentCaptor<LinhaBase> captor = ArgumentCaptor.forClass(LinhaBase.class);
        verify(linhaBaseRepository, times(7)).save(captor.capture());

        LinhaBase fc = captor.getAllValues().stream()
                .filter(l -> "frequencia_cardiaca".equals(l.getSinal())).findFirst().orElseThrow();
        assertThat(fc.getPacienteId()).isEqualTo(1);
        assertThat(fc.getDataInicio()).isEqualTo(LocalDate.now().minusDays(13));
        assertThat(fc.getDataFim()).isEqualTo(LocalDate.now());
        assertThat(fc.getDesvioPadrao()).isGreaterThan(0);
        assertThat(fc.getLimiteInferior()).isLessThan(fc.getMedia());
        assertThat(fc.getLimiteSuperior()).isGreaterThan(fc.getMedia());
        assertThat(fc.getDataFormacao()).isNotNull();

        LinhaBase saturacao = captor.getAllValues().stream()
                .filter(l -> "saturacao".equals(l.getSinal())).findFirst().orElseThrow();
        assertThat(saturacao.getMedia()).isEqualTo(98.0);
        assertThat(saturacao.getDesvioPadrao()).isEqualTo(0.0);
        assertThat(saturacao.getLimiteInferior()).isNull();
        assertThat(saturacao.getLimiteSuperior()).isNull();
    }

    @Test
    void testAtualizarLinhasBaseUsaMediaDiariaComVariasMedicoes() {
        List<SinaisVitais> lista = diasSeguidos(14);
        SinaisVitais extra = registro(0, 90);
        lista.add(extra);
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(lista);
        when(linhaBaseRepository.existsByPacienteIdAndSinal(1, "frequencia_cardiaca")).thenReturn(false);
        when(linhaBaseRepository.existsByPacienteIdAndSinal(eq(1), argThat(s -> !"frequencia_cardiaca".equals(s)))).thenReturn(true);

        service.atualizarLinhasBase(1);

        ArgumentCaptor<LinhaBase> captor = ArgumentCaptor.forClass(LinhaBase.class);
        verify(linhaBaseRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getSinal()).isEqualTo("frequencia_cardiaca");
    }

    @Test
    void testAtualizarLinhasBaseNaoFormaComDiaSemRegistro() {
        List<SinaisVitais> lista = diasSeguidos(14);
        lista.remove(5);
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(lista);

        service.atualizarLinhasBase(1);

        verify(linhaBaseRepository, never()).save(any());
    }

    @Test
    void testAtualizarLinhasBaseIgnoraValoresNulos() {
        List<SinaisVitais> lista = diasSeguidos(14);
        lista.forEach(s -> s.setPeso(null));
        lista.get(0).setDataRegistro(null);
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(lista);

        service.atualizarLinhasBase(1);

        verify(linhaBaseRepository, never()).save(any());
    }

    @Test
    void testAtualizarLinhasBaseNaoRecalculaReferenciaExistente() {
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(diasSeguidos(14));
        when(linhaBaseRepository.existsByPacienteIdAndSinal(eq(1), anyString())).thenReturn(true);

        service.atualizarLinhasBase(1);

        verify(linhaBaseRepository, never()).save(any());
    }

    @Test
    void testAtualizarLinhasBasePacienteNaoEncontrado() {
        when(usuarioRepository.buscarPorIdComBloqueio(2)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizarLinhasBase(2));
    }

    @Test
    void testCalcularProgressoComRegistroHoje() {
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(diasSeguidos(5));

        assertThat(service.calcularProgresso(1, "frequencia_cardiaca")).isEqualTo(5);
    }

    @Test
    void testCalcularProgressoSemRegistroHojeContaAPartirDeOntem() {
        List<SinaisVitais> lista = new ArrayList<>(List.of(registro(3, 70), registro(2, 71), registro(1, 72)));
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(lista);

        assertThat(service.calcularProgresso(1, "frequencia_cardiaca")).isEqualTo(3);
    }

    @Test
    void testCalcularProgressoZeradoQuandoSequenciaQuebrou() {
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(List.of(registro(5, 70)));

        assertThat(service.calcularProgresso(1, "frequencia_cardiaca")).isEqualTo(0);
    }

    @Test
    void testCalcularProgressoLimitadoAQuatorzeDias() {
        when(sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(1)).thenReturn(diasSeguidos(20));

        assertThat(service.calcularProgresso(1, "frequencia_cardiaca")).isEqualTo(14);
    }

    @Test
    void testComparar() {
        LinhaBase base = new LinhaBase();
        base.setLimiteInferior(60.0);
        base.setLimiteSuperior(80.0);

        assertThat(service.comparar(base, 50)).isEqualTo("abaixo");
        assertThat(service.comparar(base, 90)).isEqualTo("acima");
        assertThat(service.comparar(base, 70)).isEqualTo("dentro");
        assertThat(service.comparar(base, null)).isNull();
        assertThat(service.comparar(null, 70)).isNull();

        LinhaBase semFaixa = new LinhaBase();
        semFaixa.setLimiteInferior(60.0);
        assertThat(service.comparar(semFaixa, 70)).isNull();
    }

    @Test
    void testValorSinal() {
        SinaisVitais sinais = registro(0, 72);

        assertThat(service.valorSinal(sinais, "peso")).isEqualTo(sinais.getPeso());
        assertThat(service.valorSinal(sinais, "frequencia_cardiaca")).isEqualTo(72);
        assertThat(service.valorSinal(sinais, "frequencia_respiratoria")).isEqualTo(16);
        assertThat(service.valorSinal(sinais, "pressao_sistolica")).isEqualTo(sinais.getPaSistolica());
        assertThat(service.valorSinal(sinais, "pressao_diastolica")).isEqualTo(80);
        assertThat(service.valorSinal(sinais, "temperatura")).isEqualTo(36.5);
        assertThat(service.valorSinal(sinais, "saturacao")).isEqualTo(98);
        assertThat(service.valorSinal(sinais, "outro")).isNull();
    }
}
