package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ContatoEmergenciaOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        ContatoEmergenciaOutputDTO dto = new ContatoEmergenciaOutputDTO();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(1);

        dto.setId(1);
        dto.setNome("Maria");
        dto.setTelefone("41999999999");
        dto.setDataRegistro(now);
        dto.setDataModificacao(later);

        assertEquals(1, dto.getId());
        assertEquals("Maria", dto.getNome());
        assertEquals("41999999999", dto.getTelefone());
        assertEquals(now, dto.getDataRegistro());
        assertEquals(later, dto.getDataModificacao());
    }
}
