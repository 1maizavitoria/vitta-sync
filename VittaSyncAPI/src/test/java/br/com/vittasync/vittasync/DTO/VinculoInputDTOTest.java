package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VinculoInputDTOTest {

    @Test
    void testGettersAndSetters() {
        VinculoInputDTO dto = new VinculoInputDTO();

        dto.setCodigo("ABC123");
        dto.setFuncao("Responsável");

        assertEquals("ABC123", dto.getCodigo());
        assertEquals("Responsável", dto.getFuncao());
    }
}
