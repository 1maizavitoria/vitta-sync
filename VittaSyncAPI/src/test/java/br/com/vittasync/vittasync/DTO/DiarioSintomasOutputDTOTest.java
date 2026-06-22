package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DiarioSintomasOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        DiarioSintomasOutputDTO dto = new DiarioSintomasOutputDTO();

        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();

        dto.setId(1);
        dto.setSintoma("Febre");
        dto.setIntensidadeDor(8);
        dto.setDataReferencia(hoje);
        dto.setDataRegistro(agora);
        dto.setDataModificacao(agora.plusHours(2));

        assertEquals(1, dto.getId());
        assertEquals("Febre", dto.getSintoma());
        assertEquals(8, dto.getIntensidadeDor());
        assertEquals(hoje, dto.getDataReferencia());
        assertEquals(agora, dto.getDataRegistro());
        assertEquals(agora.plusHours(2), dto.getDataModificacao());
    }
}
