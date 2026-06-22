package br.com.vittasync.vittasync.Scheduler;

import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.LembreteMedicaoService;
import br.com.vittasync.vittasync.Service.NotificacaoService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;

class LembreteMedicaoSchedulerTest {

    @Test
    void testEnviarLembretes() {
        LembreteMedicaoService service = mock(LembreteMedicaoService.class);
        NotificacaoService notificacaoService = mock(NotificacaoService.class);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Paciente Teste");
        usuario.setEmail("paciente@test.com");
        LocalDateTime agora = LocalDateTime.now();
        LocalTime horaAtual = agora.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setAtivo(true);
        lembrete.setDiasSemana(agora.getDayOfWeek().name());
        lembrete.setHorario(horaAtual);
        lembrete.setEnviarEmail(true);
        lembrete.setEnviarSms(true);

        when(service.searchLembreteAtivo()).thenReturn(List.of(lembrete));
        LembreteMedicaoScheduler scheduler = new LembreteMedicaoScheduler(service, notificacaoService);
        scheduler.enviarLembretes();
        verify(notificacaoService, times(1)).enviarLembrete(usuario,
                "Este é um lembrete para registrar suas medições de saúde na plataforma VittaSync.",
                "email");

        verify(notificacaoService, times(1)).enviarLembrete(usuario,
                "Este é um lembrete para registrar suas medições de saúde na plataforma VittaSync.",
                "sms");
    }
}
