package br.com.vittasync.vittasync.Scheduler;


import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Service.EstabilidadeClinicaService;
import br.com.vittasync.vittasync.Service.SinaisVitaisService;
import br.com.vittasync.vittasync.Service.HabitosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class EstabilidadeClinicaScheduler {

    private final EstabilidadeClinicaService estabilidadeClinicaService;
    private final SinaisVitaisService sinaisVitaisService;
    private final HabitosService habitosService;

    public EstabilidadeClinicaScheduler(EstabilidadeClinicaService estabilidadeClinicaService,
                                        SinaisVitaisService sinaisVitaisService,
                                        HabitosService habitosService) {
        this.estabilidadeClinicaService = estabilidadeClinicaService;
        this.sinaisVitaisService = sinaisVitaisService;
        this.habitosService = habitosService;
    }

    @Scheduled(fixedRate = 60000)
    public void checarMudancasEstabilidade() {
        List<Integer> pacientesIds = sinaisVitaisService.findTodosPacientesIds();

        for (Integer pacienteId : pacientesIds) {
            List<SinaisVitais> sinais = sinaisVitaisService.findByPacienteId(pacienteId);
            List<Habitos> habitos = habitosService.findByPacienteId(pacienteId);

            estabilidadeClinicaService.verificarMudancaEstabilidade(pacienteId, sinais, habitos);
        }
    }
}
