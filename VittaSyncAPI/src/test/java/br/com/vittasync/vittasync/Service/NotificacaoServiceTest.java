package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.ContatoEmergencia;
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

    private ContatoEmergencia contato(boolean email, boolean sms) {
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setPaciente(usuario);
        contato.setNome("Maria");
        contato.setEmail("maria@teste.com");
        contato.setTelefone("41988887777");
        contato.setCanalEmail(email);
        contato.setCanalSms(sms);
        return contato;
    }

    @Test
    void testEnviarDesvioLinhaBasePorEmailESms() {
        service.enviarDesvioLinhaBase(1, "Maria", "maria@teste.com", "41988887777",
                true, true, "João", "Peso: 90 kg");

        verify(emailService).enviarEmailPersonalizado(eq("maria@teste.com"), contains("João"), contains("Peso: 90 kg"));
        verify(smsService).enviarDesvioLinhaBase("41988887777", "João", "Peso: 90 kg");
    }

    @Test
    void testEnviarDesvioLinhaBaseFalhaNoEmailNaoImpedeSms() {
        doThrow(new RuntimeException("falha")).when(emailService)
                .enviarEmailPersonalizado(anyString(), anyString(), anyString());

        service.enviarDesvioLinhaBase(1, "Maria", "maria@teste.com", "41988887777",
                true, true, "João", "Peso: 90 kg");

        verify(smsService).enviarDesvioLinhaBase("41988887777", "João", "Peso: 90 kg");
    }

    @Test
    void testEnviarDesvioLinhaBaseSemCanais() {
        service.enviarDesvioLinhaBase(1, "Maria", "maria@teste.com", "41988887777",
                false, false, "João", "Peso: 90 kg");

        verifyNoInteractions(emailService, smsService);
    }

    @Test
    void testEnviarAlertaEmergenciaPorCategoria() {
        ContatoEmergencia contato = contato(true, true);

        service.enviarAlertaEmergencia(contato, "Mensagem", "saudavel");
        service.enviarAlertaEmergencia(contato, "Mensagem", "moderado");
        service.enviarAlertaEmergencia(contato, "Mensagem", "critico");
        service.enviarAlertaEmergencia(contato, "Mensagem", "outra");

        verify(emailService).enviarEmailPersonalizado(eq("maria@teste.com"), contains("SAUDÁVEL"), contains("Olá, Maria"));
        verify(emailService).enviarEmailPersonalizado(eq("maria@teste.com"), contains("MODERADA"), anyString());
        verify(emailService).enviarEmailPersonalizado(eq("maria@teste.com"), contains("CRÍTICA"), anyString());
        verify(emailService).enviarEmailPersonalizado(eq("maria@teste.com"), eq("ℹ️ VittaSync - Paciente João"), anyString());
        verify(smsService, times(4)).enviarAlertaClinico(eq("41988887777"), eq("Maria"), eq("João"), anyString(), eq("Mensagem"));
    }

    @Test
    void testEnviarAlertaEmergenciaSemCanais() {
        service.enviarAlertaEmergencia(contato(false, false), "Mensagem", "critico");

        verifyNoInteractions(emailService, smsService);
    }

    @Test
    void testEnviarAlertaRepouso() {
        ContatoEmergencia contato = contato(true, true);

        service.enviarAlertaRepouso(contato, "sms");
        service.enviarAlertaRepouso(contato, "email");
        service.enviarAlertaRepouso(contato, "ambos");

        verify(smsService, times(2)).enviarAlertaRepouso("41988887777", "João");
        verify(emailService, times(2)).enviarAlertaRepouso("maria@teste.com", "João");
        assertThrows(RuntimeException.class, () -> service.enviarAlertaRepouso(contato, "push"));
    }
}
