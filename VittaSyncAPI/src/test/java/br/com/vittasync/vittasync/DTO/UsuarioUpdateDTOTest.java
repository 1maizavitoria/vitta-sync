package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioUpdateDTOTest {

    @Test
    void testGettersAndSetters() {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();

        LocalDate nascimento = LocalDate.of(1992, 8, 10);

        dto.setNome("Carlos Souza");
        dto.setEmail("carlos@exemplo.com");
        dto.setTelefone("41977776666");
        dto.setAltura(1.75);
        dto.setPesoInicial(80.0);
        dto.setFuncaoResponsavel("Pai");
        dto.setDataNascimento(nascimento);

        assertEquals("Carlos Souza", dto.getNome());
        assertEquals("carlos@exemplo.com", dto.getEmail());
        assertEquals("41977776666", dto.getTelefone());
        assertEquals(1.75, dto.getAltura());
        assertEquals(80.0, dto.getPesoInicial());
        assertEquals("Pai", dto.getFuncaoResponsavel());
        assertEquals(nascimento, dto.getDataNascimento());
    }
}
