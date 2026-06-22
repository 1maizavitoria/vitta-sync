package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class LembreteMedicaoOutputDTOTest {

    @Test
    void testConstructorAndGetters() {
        LocalTime horario = LocalTime.of(9, 15);

        LembreteMedicaoOutputDTO dto = new LembreteMedicaoOutputDTO(
                1L,
                "Segunda, Terça",
                horario,
                true,
                true,
                false
        );

        assertEquals(1L, dto.getId());
        assertEquals("Segunda, Terça", dto.getDiasSemana());
        assertEquals(horario, dto.getHorario());
        assertTrue(dto.isAtivo());
        assertTrue(dto.getEnviarEmail());
        assertFalse(dto.getEnviarSms());
    }
}
