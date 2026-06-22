package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PacienteResumoDTOTest {

    @Test
    void testGettersAndSetters() {
        PacienteResumoDTO dto = new PacienteResumoDTO();

        dto.setId(1);
        dto.setNome("João");
        dto.setEmail("joao@exemplo.com");
        dto.setCpf("12345678900");

        assertEquals(1, dto.getId());
        assertEquals("João", dto.getNome());
        assertEquals("joao@exemplo.com", dto.getEmail());
        assertEquals("12345678900", dto.getCpf());
    }

    @Test
    void testConstructorWithArguments() {
        PacienteResumoDTO dto = new PacienteResumoDTO(
                2,
                "Maria",
                "maria@exemplo.com",
                "98765432100"
        );

        assertEquals(2, dto.getId());
        assertEquals("Maria", dto.getNome());
        assertEquals("maria@exemplo.com", dto.getEmail());
        assertEquals("98765432100", dto.getCpf());
    }
}
