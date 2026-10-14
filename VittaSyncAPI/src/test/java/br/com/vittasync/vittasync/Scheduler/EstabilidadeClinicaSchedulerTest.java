package br.com.vittasync.vittasync.Scheduler;


import br.com.vittasync.vittasync.Service.EstabilidadeClinicaService;
import br.com.vittasync.vittasync.Service.SinaisVitaisService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;


class EstabilidadeClinicaSchedulerTest {

    @Test
    void testChecarMudancasEstabilidadeDeTodosOsPacientes() {
        EstabilidadeClinicaService estabilidadeClinicaService = mock(EstabilidadeClinicaService.class);
        SinaisVitaisService sinaisVitaisService = mock(SinaisVitaisService.class);
        EstabilidadeClinicaScheduler scheduler = new EstabilidadeClinicaScheduler(
                estabilidadeClinicaService, sinaisVitaisService);

        when(sinaisVitaisService.findTodosPacientesIds()).thenReturn(List.of(1, 2));

        scheduler.checarMudancasEstabilidade();

        verify(estabilidadeClinicaService).verificarMudancaEstabilidade(1);
        verify(estabilidadeClinicaService).verificarMudancaEstabilidade(2);
    }
}
