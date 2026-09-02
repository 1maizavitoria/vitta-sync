package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class HabitosInputDTOTest {

    @Test
    void testGettersAndSetters() {
        HabitosInputDTO dto = new HabitosInputDTO();

        LocalDate hoje = LocalDate.now();

        dto.setHorasSono(8);
        dto.setMinutosExercicio(45);
        dto.setDataReferencia(hoje);
        dto.setCanal("email");

        assertEquals(8, dto.getHorasSono());
        assertEquals(45, dto.getMinutosExercicio());
        assertEquals(hoje, dto.getDataReferencia());
        assertEquals("email", dto.getCanal());
    }
}
