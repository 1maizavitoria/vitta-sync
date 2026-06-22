package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HabitosOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        HabitosOutputDTO dto = new HabitosOutputDTO();

        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();

        dto.setId(1);
        dto.setHorasSono(7);
        dto.setMinutosExercicio(30);
        dto.setDataReferencia(hoje);
        dto.setDataRegistro(agora);
        dto.setDataModificacao(agora.plusDays(1));

        assertEquals(1, dto.getId());
        assertEquals(7, dto.getHorasSono());
        assertEquals(30, dto.getMinutosExercicio());
        assertEquals(hoje, dto.getDataReferencia());
        assertEquals(agora, dto.getDataRegistro());
        assertEquals(agora.plusDays(1), dto.getDataModificacao());
    }
}
