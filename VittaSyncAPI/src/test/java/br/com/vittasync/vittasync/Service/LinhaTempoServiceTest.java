package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.LinhaTempoItemDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoResponseDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class LinhaTempoServiceTest {

    private HabitosRepository habitosRepository;
    private SinaisVitaisRepository sinaisVitaisRepository;
    private DiarioSintomasRepository diarioSintomasRepository;
    private PermissaoService permissaoService;
    private LinhaTempoService service;

    @BeforeEach
    void setup() {
        habitosRepository = mock(HabitosRepository.class);
        sinaisVitaisRepository = mock(SinaisVitaisRepository.class);
        diarioSintomasRepository = mock(DiarioSintomasRepository.class);
        permissaoService = mock(PermissaoService.class);

        service = new LinhaTempoService(habitosRepository, sinaisVitaisRepository, diarioSintomasRepository, permissaoService);
    }

    @Test
    void testConsultarLinhaTempoOrdenaItensDoMaisRecente() {
        LocalDate hoje = LocalDate.now();

        SinaisVitais sinais = new SinaisVitais();
        sinais.setId(1);
        sinais.setDataRegistro(hoje.minusDays(2).atTime(10, 0));
        sinais.setFcBpm(80);

        Habitos habito = new Habitos();
        habito.setId(2);
        habito.setDataReferencia(hoje);
        habito.setDataRegistro(hoje.atTime(8, 0));
        habito.setHorasSono(7);

        DiarioSintomas sintoma = new DiarioSintomas();
        sintoma.setId(3);
        sintoma.setDataReferencia(hoje);
        sintoma.setDataRegistro(hoje.atTime(12, 0));
        sintoma.setSintoma("Dor");

        DiarioSintomas sintomaSemData = new DiarioSintomas();
        sintomaSemData.setId(4);
        sintomaSemData.setSintoma("Tontura");

        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);
        when(sinaisVitaisRepository.findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(eq(2), any(), any()))
                .thenReturn(List.of(sinais));
        when(habitosRepository.findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(eq(2), any(), any()))
                .thenReturn(List.of(habito));
        when(diarioSintomasRepository.findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(eq(2), any(), any()))
                .thenReturn(List.of(sintoma, sintomaSemData));

        LinhaTempoResponseDTO resposta = service.consultarLinhaTempo(1, 2, null, null);

        assertThat(resposta.getSinaisVitais()).hasSize(1);
        assertThat(resposta.getHabitos()).hasSize(1);
        assertThat(resposta.getSintomas()).hasSize(2);
        assertThat(resposta.getItens()).extracting(LinhaTempoItemDTO::getTipo)
                .containsExactly("SINTOMA", "HABITO", "SINAL_VITAL", "SINTOMA");
        assertThat(resposta.getItens().get(0).getId()).isEqualTo(3);

        verify(habitosRepository).findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(2, hoje.minusDays(6), hoje);
    }

    @Test
    void testConsultarLinhaTempoComPeriodoInformado() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 10);
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);

        LinhaTempoResponseDTO resposta = service.consultarLinhaTempo(1, 2, inicio, fim);

        assertThat(resposta.getItens()).isEmpty();
        verify(diarioSintomasRepository).findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(2, inicio, fim);
    }

    @Test
    void testConsultarLinhaTempoSemPermissao() {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(false);

        assertThrows(AcessoNegadoException.class, () -> service.consultarLinhaTempo(1, 2, null, null));
    }

    @Test
    void testConsultarLinhaTempoPeriodoInvalido() {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);

        assertThrows(DadosInvalidosException.class,
                () -> service.consultarLinhaTempo(1, 2, LocalDate.now(), LocalDate.now().minusDays(1)));
    }
}
