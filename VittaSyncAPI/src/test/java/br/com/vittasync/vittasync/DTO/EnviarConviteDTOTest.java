package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnviarConviteDTOTest {

    @Test
    void testGettersAndSetters() {
        EnviarConviteDTO dto = new EnviarConviteDTO();

        dto.setEmail("teste@exemplo.com");
        dto.setCodigo("ABC123");

        assertEquals("teste@exemplo.com", dto.getEmail());
        assertEquals("ABC123", dto.getCodigo());
    }
}
