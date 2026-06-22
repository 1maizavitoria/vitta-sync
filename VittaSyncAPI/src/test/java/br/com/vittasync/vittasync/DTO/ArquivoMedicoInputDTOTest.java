package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArquivoMedicoInputDTOTest {

    @Test
    void testGettersAndSetters() {
        ArquivoMedicoInputDTO dto = new ArquivoMedicoInputDTO();

        dto.setNomeArquivo("exame");
        dto.setExtensao("pdf");

        assertEquals("exame", dto.getNomeArquivo());
        assertEquals("pdf", dto.getExtensao());
    }
}
