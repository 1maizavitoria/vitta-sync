package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        UsuarioOutputDTO dto = new UsuarioOutputDTO();

        LocalDate nascimento = LocalDate.of(1985, 3, 15);
        LocalDateTime cadastro = LocalDateTime.now();

        dto.setCpf("12345678901");
        dto.setNome("Maria Oliveira");
        dto.setEmail("maria@exemplo.com");
        dto.setTelefone("41988887777");
        dto.setPesoInicial(65.0);
        dto.setAltura(1.70);
        dto.setTipo("responsavel");
        dto.setConselho("CRM98765");
        dto.setFuncaoResponsavel("Mãe");
        dto.setDataNascimento(nascimento);
        dto.setDataCadastro(cadastro);

        assertEquals("12345678901", dto.getCpf());
        assertEquals("Maria Oliveira", dto.getNome());
        assertEquals("maria@exemplo.com", dto.getEmail());
        assertEquals("41988887777", dto.getTelefone());
        assertEquals(65.0, dto.getPesoInicial());
        assertEquals(1.70, dto.getAltura());
        assertEquals("responsavel", dto.getTipo());
        assertEquals("CRM98765", dto.getConselho());
        assertEquals("Mãe", dto.getFuncaoResponsavel());
        assertEquals(nascimento, dto.getDataNascimento());
        assertEquals(cadastro, dto.getDataCadastro());
    }
}
