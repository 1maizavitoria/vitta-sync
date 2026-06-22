package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ArquivoMedicoOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        ArquivoMedicoOutputDTO dto = new ArquivoMedicoOutputDTO();

        LocalDateTime now = LocalDateTime.now();

        dto.setId(1);
        dto.setNomeArquivo("exame");
        dto.setDataUpload(now);
        dto.setPacienteCpf("12345678900");
        dto.setExtensao("pdf");
        dto.setNomeOriginal("exame_original.pdf");
        dto.setMedicoNome("Dr. Silva");
        dto.setPacienteNome("João");

        assertEquals(1, dto.getId());
        assertEquals("exame", dto.getNomeArquivo());
        assertEquals(now, dto.getDataUpload());
        assertEquals("12345678900", dto.getPacienteCpf());
        assertEquals("pdf", dto.getExtensao());
        assertEquals("exame_original.pdf", dto.getNomeOriginal());
        assertEquals("Dr. Silva", dto.getMedicoNome());
        assertEquals("João", dto.getPacienteNome());
    }
}
