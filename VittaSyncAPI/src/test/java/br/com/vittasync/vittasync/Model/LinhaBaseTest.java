package br.com.vittasync.vittasync.Model;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class LinhaBaseTest {
    @Test
    void testLinhaBase() {
        LinhaBase linhaBase = new LinhaBase();
        LocalDate inicio = LocalDate.of(2026, 9, 16);
        LocalDate fim = LocalDate.of(2026, 9, 29);
        LocalDateTime formacao = LocalDateTime.now();

        linhaBase.setId(1);
        linhaBase.setPacienteId(2);
        linhaBase.setSinal("peso");
        linhaBase.setDataInicio(inicio);
        linhaBase.setDataFim(fim);
        linhaBase.setMedia(70.5);
        linhaBase.setDesvioPadrao(1.2);
        linhaBase.setLimiteInferior(68.1);
        linhaBase.setLimiteSuperior(72.9);
        linhaBase.setDataFormacao(formacao);

        assertEquals(1, linhaBase.getId());
        assertEquals(2, linhaBase.getPacienteId());
        assertEquals("peso", linhaBase.getSinal());
        assertEquals(inicio, linhaBase.getDataInicio());
        assertEquals(fim, linhaBase.getDataFim());
        assertEquals(70.5, linhaBase.getMedia());
        assertEquals(1.2, linhaBase.getDesvioPadrao());
        assertEquals(68.1, linhaBase.getLimiteInferior());
        assertEquals(72.9, linhaBase.getLimiteSuperior());
        assertEquals(formacao, linhaBase.getDataFormacao());
    }
}
