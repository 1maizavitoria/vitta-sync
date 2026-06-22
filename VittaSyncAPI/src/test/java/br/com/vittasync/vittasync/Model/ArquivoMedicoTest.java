package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.ArquivoMedico;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class ArquivoMedicoTest {
    @Test
    void testArquivoMedico() {
        ArquivoMedico arquivo = new ArquivoMedico();
        Usuario medico = new Usuario();
        Usuario paciente = new Usuario();

        arquivo.setId(1);
        arquivo.setMedico(medico);
        arquivo.setPaciente(paciente);
        arquivo.setNomeArquivo("exame123");
        arquivo.setArquivo(new byte[]{1,2,3});
        arquivo.setDataUpload(LocalDateTime.now());
        arquivo.setExtensao("pdf");
        arquivo.setNomeOriginal("exame.pdf");

        assertEquals(1, arquivo.getId());
        assertEquals(medico, arquivo.getMedico());
        assertEquals(paciente, arquivo.getPaciente());
        assertEquals("exame123", arquivo.getNomeArquivo());
        assertArrayEquals(new byte[]{1,2,3}, arquivo.getArquivo());
        assertNotNull(arquivo.getDataUpload());
        assertEquals("pdf", arquivo.getExtensao());
        assertEquals("exame.pdf", arquivo.getNomeOriginal());
    }
}
