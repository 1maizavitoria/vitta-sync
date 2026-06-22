package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

class VinculoOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        VinculoOutputDTO dto = new VinculoOutputDTO();

        Timestamp agora = new Timestamp(System.currentTimeMillis());

        dto.setId(10L);
        dto.setNome("Ana Paula");
        dto.setEmail("ana@exemplo.com");
        dto.setTipo("saude");
        dto.setConselho("CRM12345");
        dto.setFuncao("Médica");
        dto.setCriadoEm(agora);

        assertEquals(10L, dto.getId());
        assertEquals("Ana Paula", dto.getNome());
        assertEquals("ana@exemplo.com", dto.getEmail());
        assertEquals("saude", dto.getTipo());
        assertEquals("CRM12345", dto.getConselho());
        assertEquals("Médica", dto.getFuncao());
        assertEquals(agora, dto.getCriadoEm());
    }
}
