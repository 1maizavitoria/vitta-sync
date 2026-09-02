package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HabitosInputDTOTest {

    @Test
    void testGettersAndSetters() {
        HabitosInputDTO dto = new HabitosInputDTO();

        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();

        dto.setHorasSono(8);
        dto.setMinutosExercicio(45);
        dto.setDataReferencia(hoje);
        dto.setDataRegistro(agora);
        dto.setDataModificacao(agora.plusHours(1));

        assertEquals(8, dto.getHorasSono());
        assertEquals(45, dto.getMinutosExercicio());
        assertEquals(hoje, dto.getDataReferencia());
        assertEquals(agora, dto.getDataRegistro());
        assertEquals(agora.plusHours(1), dto.getDataModificacao());
    }
}
