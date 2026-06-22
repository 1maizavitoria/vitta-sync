package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitarRedefinicaoDTOTest {

    @Test
    void testGettersAndSetters() {
        SolicitarRedefinicaoDTO dto = new SolicitarRedefinicaoDTO();

        dto.setEmail("usuario@exemplo.com");
        dto.setCanal("WEB");

        assertEquals("usuario@exemplo.com", dto.getEmail());
        assertEquals("WEB", dto.getCanal());
    }
}
