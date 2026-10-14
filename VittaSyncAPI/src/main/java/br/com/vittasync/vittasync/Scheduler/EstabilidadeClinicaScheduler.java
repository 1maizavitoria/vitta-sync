package br.com.vittasync.vittasync.Scheduler;


import br.com.vittasync.vittasync.Service.EstabilidadeClinicaService;
import br.com.vittasync.vittasync.Service.SinaisVitaisService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class EstabilidadeClinicaScheduler {

    private final EstabilidadeClinicaService estabilidadeClinicaService;
    private final SinaisVitaisService sinaisVitaisService;

    public EstabilidadeClinicaScheduler(EstabilidadeClinicaService estabilidadeClinicaService,
                                        SinaisVitaisService sinaisVitaisService) {
        this.estabilidadeClinicaService = estabilidadeClinicaService;
        this.sinaisVitaisService = sinaisVitaisService;
    }

    @Scheduled(fixedRate = 60000)
    public void checarMudancasEstabilidade() {
        List<Integer> pacientesIds = sinaisVitaisService.findTodosPacientesIds();

        for (Integer pacienteId : pacientesIds) {
            estabilidadeClinicaService.verificarMudancaEstabilidade(pacienteId);
        }
    }
}
