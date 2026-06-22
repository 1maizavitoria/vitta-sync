package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificacaoServiceTest {

    private EmailService emailService;
    private SmsService smsService;
    private NotificacaoService service;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        emailService = mock(EmailService.class);
        smsService = mock(SmsService.class);
        service = new NotificacaoService(emailService, smsService);

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("João");
        usuario.setEmail("joao@teste.com");
        usuario.setTelefone("999999999");
    }

    @Test
    void testEnviarCodigoViaSms() {
        service.enviarCodigo(usuario, "12345", "sms");

        verify(smsService).enviarCodigo("999999999", "12345");
        verifyNoInteractions(emailService);
    }

    @Test
    void testEnviarCodigoViaEmail() {
        service.enviarCodigo(usuario, "12345", "email");

        verify(emailService).enviarCodigo("joao@teste.com", "12345");
        verifyNoInteractions(smsService);
    }

    @Test
    void testEnviarCodigoCanalInvalidoLancaExcecao() {
        assertThrows(RuntimeException.class,
                () -> service.enviarCodigo(usuario, "12345", "push"));
    }

    @Test
    void testEnviarLembreteViaSms() {
        service.enviarLembrete(usuario, "Não esqueça de medir a pressão", "sms");

        verify(smsService).enviarLembrete("999999999", "João", "Não esqueça de medir a pressão");
        verifyNoInteractions(emailService);
    }

    @Test
    void testEnviarLembreteViaEmail() {
        service.enviarLembrete(usuario, "Não esqueça de medir a pressão", "email");

        verify(emailService).enviarLembrete("joao@teste.com", "João", "Não esqueça de medir a pressão");
        verifyNoInteractions(smsService);
    }

    @Test
    void testEnviarLembreteViaAmbos() {
        service.enviarLembrete(usuario, "Não esqueça de medir a pressão", "ambos");

        verify(smsService).enviarLembrete("999999999", "João", "Não esqueça de medir a pressão");
        verify(emailService).enviarLembrete("joao@teste.com", "João", "Não esqueça de medir a pressão");
    }

    @Test
    void testEnviarLembreteCanalInvalidoLancaExcecao() {
        assertThrows(RuntimeException.class,
                () -> service.enviarLembrete(usuario, "Mensagem", "telegram"));
    }
}
