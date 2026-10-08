package br.com.vittasync.vittasync.Initializer;


import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Service.LinhaBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class LinhaBaseInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(LinhaBaseInitializer.class);

    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final LinhaBaseService linhaBaseService;

    public LinhaBaseInitializer(SinaisVitaisRepository sinaisVitaisRepository,
                                LinhaBaseService linhaBaseService) {
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.linhaBaseService = linhaBaseService;
    }

    @Override
    public void run(ApplicationArguments args) {

        for (Integer pacienteId : sinaisVitaisRepository.findTodosPacientesIds()) {
            try {
                linhaBaseService.atualizarLinhasBase(pacienteId);
            } catch (RuntimeException exception) {
                logger.error("Falha ao formar linhas de base do paciente {}. "
                        + "Uma nova tentativa ocorrerá na próxima inicialização.", pacienteId, exception);
            }
        }
    }
}
