package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RedefinirSenhaDTOTest {

    @Test
    void testGettersAndSetters() {
        RedefinirSenhaDTO dto = new RedefinirSenhaDTO();

        dto.setCodigo("ABC123");
        dto.setNovaSenha("novaSenhaSegura");

        assertEquals("ABC123", dto.getCodigo());
        assertEquals("novaSenhaSegura", dto.getNovaSenha());
    }
}
