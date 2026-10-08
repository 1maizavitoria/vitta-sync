package br.com.vittasync.vittasync.Service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.lang.reflect.Field;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setup() throws Exception {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService();

        // Injetando o mock via reflexão
        Field field = EmailService.class.getDeclaredField("mailSender");
        field.setAccessible(true);
        field.set(emailService, mailSender);
    }

    @Test
    void testEnviarCodigo() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.enviarCodigo("destinatario@teste.com", "12345");

        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();

        assertThat(msg.getTo()).contains("destinatario@teste.com");
        assertThat(msg.getSubject()).isEqualTo("Código de Verificação VittaSync");
        assertThat(msg.getText()).contains("Seu código de verificação é: 12345");
    }

    @Test
    void testEnviarLembrete() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.enviarLembrete("destinatario@teste.com", "Paciente Teste", "Não esqueça de medir sua pressão.");

        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();

        assertThat(msg.getTo()).contains("destinatario@teste.com");
        assertThat(msg.getSubject()).isEqualTo("Lembrete de medição - VittaSync");
        assertThat(msg.getText()).contains("Paciente Teste");
        assertThat(msg.getText()).contains("Não esqueça de medir sua pressão.");
    }

    @Test
    void testEnviarConviteVinculo() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.enviarConviteVinculo("destinatario@teste.com", "Convidado", "Paciente Teste", "ABC123", "http://link.com");

        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();

        assertThat(msg.getTo()).contains("destinatario@teste.com");
        assertThat(msg.getSubject()).contains("Convite de vínculo com Paciente Teste");
        assertThat(msg.getText()).contains("Convidado");
        assertThat(msg.getText()).contains("Código de vínculo: ABC123");
        assertThat(msg.getText()).contains("http://link.com");
    }

    @Test
    void testEnviarAlertaClinico() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.enviarAlertaClinico("destinatario@teste.com", "Maria", "Paciente Teste", "Alerta", "Detalhes do alerta");

        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();

        assertThat(msg.getSubject()).isEqualTo("Alerta");
        assertThat(msg.getText()).contains("Olá, Maria", "Paciente Teste", "Detalhes do alerta");
    }

    @Test
    void testEnviarAlertaRepouso() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.enviarAlertaRepouso("destinatario@teste.com", "Paciente Teste");

        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();

        assertThat(msg.getFrom()).isEqualTo("no.reply.vittasync@gmail.com");
        assertThat(msg.getSubject()).contains("Repouso recomendado para paciente Paciente Teste");
        assertThat(msg.getText()).contains("precisa de repouso");
    }
}
