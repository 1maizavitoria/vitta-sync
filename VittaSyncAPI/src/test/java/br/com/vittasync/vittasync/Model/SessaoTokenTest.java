package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.SessaoToken;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class SessaoTokenTest {
    @Test
    void testSessaoToken() {
        SessaoToken token = new SessaoToken();

        token.setId(1L);
        token.setToken("abc123");
        token.setAtivo(false);
        token.setCriadoEm(LocalDateTime.now());

        assertEquals(1L, token.getId());
        assertEquals("abc123", token.getToken());
        assertFalse(token.getAtivo());
        assertNotNull(token.getCriadoEm());
    }
}
