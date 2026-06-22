package br.com.vittasync.vittasync.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class SmsServiceTest {

    private SmsService spyService;

    @BeforeEach
    void setup() {
        spyService = Mockito.spy(new SmsService());
        doNothing().when(spyService).enviarCodigo(anyString(), anyString());
        doNothing().when(spyService).enviarLembrete(anyString(), anyString(), anyString());
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
    void testFormatarTelefoneBR() throws Exception {
        var method = SmsService.class.getDeclaredMethod("formatarTelefoneBR", String.class);
        method.setAccessible(true);

        String resultado = (String) method.invoke(spyService, "999999999");

        assert resultado.equals("+55999999999");
    }
}
