package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CanalNotificacaoDTOTest {

    @Test
    void testGettersAndSetters() {
        CanalNotificacaoDTO dto = new CanalNotificacaoDTO();

        dto.setCanal("EMAIL");

        assertEquals("EMAIL", dto.getCanal());
    }
}
