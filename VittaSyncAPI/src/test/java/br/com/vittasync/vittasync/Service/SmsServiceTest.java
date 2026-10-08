package br.com.vittasync.vittasync.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;


class SmsServiceTest {

    private SmsService spyService;
    private SmsService smsService;
    private TwilioRestClient restClient;

    @BeforeEach
    void setup() {
        spyService = Mockito.spy(new SmsService());
        doNothing().when(spyService).enviarCodigo(anyString(), anyString());
        doNothing().when(spyService).enviarLembrete(anyString(), anyString(), anyString());

        smsService = new SmsService();
        ReflectionTestUtils.setField(smsService, "twilioPhone", "+15550000000");

        restClient = mock(TwilioRestClient.class);
        when(restClient.getAccountSid()).thenReturn("AC123");
        when(restClient.getObjectMapper()).thenReturn(new ObjectMapper());
        when(restClient.request(any(Request.class)))
                .thenReturn(new Response("{\"sid\":\"SM123\",\"status\":\"queued\"}", 201));
        Twilio.setRestClient(restClient);
    }

    @AfterEach
    void limpar() {
        Twilio.setRestClient(null);
    }

    private List<String> enviados() {
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(restClient, atLeastOnce()).request(captor.capture());
        return captor.getAllValues().stream()
                .map(r -> String.valueOf(r.getPostParams().get("To")) + "|" + r.getPostParams().get("Body"))
                .toList();
    }

    @Test
    void testEnviarCodigoMontaMensagemCorreta() {
        spyService.enviarCodigo("999999999", "12345");
        verify(spyService).enviarCodigo(eq("999999999"), eq("12345"));
    }

    @Test
    void testEnviarLembreteMontaMensagemCorreta() {
        spyService.enviarLembrete("999999999", "João", "Não esqueça de medir a pressão");

        verify(spyService).enviarLembrete(eq("999999999"), eq("João"), eq("Não esqueça de medir a pressão"));
    }

    @Test
    void testEnviarCodigoAdicionaDdiDoBrasil() {
        smsService.enviarCodigo("41999999999", "12345");

        assertThat(enviados().get(0)).contains("+5541999999999", "12345");
    }

    @Test
    void testEnviarLembreteMantemDdiExistente() {
        smsService.enviarLembrete("+5541999999999", "João", "Meça a pressão");

        assertThat(enviados().get(0)).contains("+5541999999999", "Olá, João. Meça a pressão");
    }

    @Test
    void testEnviarAlertaClinicoPorCategoria() {
        smsService.enviarAlertaClinico("41999999999", "Maria", "João", "saudavel", "detalhes");
        smsService.enviarAlertaClinico("41999999999", "Maria", "João", "moderado", "detalhes");
        smsService.enviarAlertaClinico("41999999999", "Maria", "João", "critico", "detalhes");
        smsService.enviarAlertaClinico("41999999999", "Maria", "João", "outra", "detalhes");

        List<String> mensagens = enviados();
        assertThat(mensagens).hasSize(4);
        assertThat(mensagens.get(0)).contains("✅");
        assertThat(mensagens.get(1)).contains("⚠️");
        assertThat(mensagens.get(2)).contains("🚨");
        assertThat(mensagens.get(3)).contains("ℹ️");
    }

    @Test
    void testEnviarAlertaRepousoEDesvioLinhaBase() {
        smsService.enviarAlertaRepouso("41999999999", "João");
        smsService.enviarDesvioLinhaBase("41999999999", "João", "Peso: 90 kg");

        List<String> mensagens = enviados();
        assertThat(mensagens.get(0)).contains("Repouso recomendado para paciente João");
        assertThat(mensagens.get(1)).contains("medição fora do padrão individual", "Peso: 90 kg");
    }

    @Test
    void testFalhaNoEnvioNaoPropagaErro() {
        when(restClient.request(any(Request.class))).thenThrow(new RuntimeException("sem conexão"));

        assertDoesNotThrow(() -> smsService.enviarCodigo("41999999999", "12345"));
    }
}
