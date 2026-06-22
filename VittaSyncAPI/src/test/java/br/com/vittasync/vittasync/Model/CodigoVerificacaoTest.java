package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class CodigoVerificacaoTest {
    @Test
    void testCodigoVerificacao() {
        CodigoVerificacao codigo = new CodigoVerificacao();
        Usuario usuario = new Usuario();

        codigo.setId(1);
        codigo.setUsuario(usuario);
        codigo.setCodigo("ABC12");
        codigo.setTipo("login");
        codigo.setExpira(LocalDateTime.now().plusMinutes(10));
        codigo.setUtilizado(true);

        assertEquals(1, codigo.getId());
        assertEquals(usuario, codigo.getUsuario());
        assertEquals("ABC12", codigo.getCodigo());
        assertEquals("login", codigo.getTipo());
        assertNotNull(codigo.getExpira());
        assertTrue(codigo.getUtilizado());
    }
}
