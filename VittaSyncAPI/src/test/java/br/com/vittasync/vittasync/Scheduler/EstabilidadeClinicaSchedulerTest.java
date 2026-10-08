package br.com.vittasync.vittasync.Scheduler;


import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Service.EstabilidadeClinicaService;
import br.com.vittasync.vittasync.Service.HabitosService;
import br.com.vittasync.vittasync.Service.SinaisVitaisService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;


class EstabilidadeClinicaSchedulerTest {

    @Test
    void testChecarMudancasEstabilidadeDeTodosOsPacientes() {
        EstabilidadeClinicaService estabilidadeClinicaService = mock(EstabilidadeClinicaService.class);
        SinaisVitaisService sinaisVitaisService = mock(SinaisVitaisService.class);
        HabitosService habitosService = mock(HabitosService.class);
        EstabilidadeClinicaScheduler scheduler = new EstabilidadeClinicaScheduler(
                estabilidadeClinicaService, sinaisVitaisService, habitosService);

        List<SinaisVitais> sinais = List.of(new SinaisVitais());
        List<Habitos> habitos = List.of(new Habitos());

        when(sinaisVitaisService.findTodosPacientesIds()).thenReturn(List.of(1, 2));
        when(sinaisVitaisService.findByPacienteId(1)).thenReturn(sinais);
        when(habitosService.findByPacienteId(1)).thenReturn(habitos);
        when(sinaisVitaisService.findByPacienteId(2)).thenReturn(List.of());
        when(habitosService.findByPacienteId(2)).thenReturn(List.of());

        scheduler.checarMudancasEstabilidade();

        verify(estabilidadeClinicaService).verificarMudancaEstabilidade(1, sinais, habitos);
        verify(estabilidadeClinicaService).verificarMudancaEstabilidade(2, List.of(), List.of());
    }
}
