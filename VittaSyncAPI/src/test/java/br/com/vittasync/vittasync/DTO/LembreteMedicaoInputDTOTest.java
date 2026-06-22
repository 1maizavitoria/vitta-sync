package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class LembreteMedicaoInputDTOTest {

    @Test
    void testGettersAndSetters() {
        LembreteMedicaoInputDTO dto = new LembreteMedicaoInputDTO();

        LocalTime horario = LocalTime.of(8, 30);

        dto.setDiasSemana("Segunda, Quarta, Sexta");
        dto.setHorario(horario);
        dto.setAtivo(true);
        dto.setEnviarEmail(true);
        dto.setEnviarSms(false);

        assertEquals("Segunda, Quarta, Sexta", dto.getDiasSemana());
        assertEquals(horario, dto.getHorario());
        assertTrue(dto.isAtivo());
        assertTrue(dto.getEnviarEmail());
        assertFalse(dto.getEnviarSms());
    }
}
