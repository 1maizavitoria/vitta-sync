package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginDTOTest {

    @Test
    void testGettersAndSetters() {
        LoginDTO dto = new LoginDTO();

        dto.setCpf("12345678900");
        dto.setSenha("senhaSegura");
        dto.setCanal("WEB");

        assertEquals("12345678900", dto.getCpf());
        assertEquals("senhaSegura", dto.getSenha());
        assertEquals("WEB", dto.getCanal());
    }
}
