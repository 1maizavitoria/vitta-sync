package br.com.vittasync.vittasync.Scheduler;


import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.LembreteMedicaoService;
import br.com.vittasync.vittasync.Service.NotificacaoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
public class LembreteMedicaoScheduler {

    private final LembreteMedicaoService service;
    private final NotificacaoService notificationService;

    public LembreteMedicaoScheduler(
            LembreteMedicaoService service,
            NotificacaoService notificationService
    ) {

        this.service = service;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void enviarLembretes() {

        LocalDateTime agora =
                LocalDateTime.now();

        DayOfWeek dia =
                agora.getDayOfWeek();

        LocalTime horaAtual =
                agora.toLocalTime()
                        .truncatedTo(ChronoUnit.MINUTES);

        List<LembreteMedicao> lembretes =
                service.searchLembreteAtivo();

        for (LembreteMedicao lembrete : lembretes) {

            LocalTime horarioLembrete =
                    lembrete.getHorario()
                            .truncatedTo(ChronoUnit.MINUTES);

            if (
                    lembrete.isAtivo()

                            && lembrete.getDiasSemana()
                            .toUpperCase()
                            .contains(dia.name())

                            && horarioLembrete.equals(horaAtual)
            ) {

                Usuario usuario =
                        lembrete.getUsuario();

                String mensagem =
                        "Este é um lembrete para "
                                + "registrar suas medições "
                                + "de saúde na plataforma "
                                + "VittaSync.";

                if (
                        Boolean.TRUE.equals(
                                lembrete.getEnviarEmail()
                        )
                ) {

                    notificationService.enviarLembrete(
                            usuario,
                            mensagem,
                            "email"
                    );
                }

                if (
                        Boolean.TRUE.equals(
                                lembrete.getEnviarSms()
                        )
                ) {

                    notificationService.enviarLembrete(
                            usuario,
                            mensagem,
                            "sms"
                    );
                }
            }
        }
    }
}