package br.com.vittasync.vittasync.Scheduler;


import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Service.LembreteMedicaoService;
import br.com.vittasync.vittasync.Service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
public class LembreteMedicaoScheduler {

    private final LembreteMedicaoService service;
    private final EmailService emailService;

    public LembreteMedicaoScheduler(LembreteMedicaoService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void enviarLembretes() {
        LocalDateTime agora = LocalDateTime.now();
        DayOfWeek dia = agora.getDayOfWeek(); // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
        LocalTime horaAtual = agora.toLocalTime().truncatedTo(ChronoUnit.MINUTES);

        List<LembreteMedicao> lembretes = service.searchLembreteAtivo();

        for (LembreteMedicao lembrete : lembretes) {
            LocalTime horarioLembrete = lembrete.getHorario().truncatedTo(ChronoUnit.MINUTES);

            if (lembrete.isAtivo()
                    && lembrete.getDiasSemana().toUpperCase().contains(dia.name())
                    && horarioLembrete.equals(horaAtual)) {

                emailService.enviarLembrete(
                        lembrete.getUsuario().getEmail(),
                        "Lembrete: registre suas medições no sistema VittaSync."
                );
            }
        }
    }
}
