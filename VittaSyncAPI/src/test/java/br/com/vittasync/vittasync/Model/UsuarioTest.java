package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class UsuarioTest {
    @Test
    void testUsuario() {
        Usuario usuario = new Usuario();

        usuario.setId(1);
        usuario.setCpf("12345678900");
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setTelefone("11999999999");
        usuario.setPesoInicial(70.0);
        usuario.setAltura(1.75);
        usuario.setFuncaoResponsavel("cuidador");
        usuario.setSenha("senhaSegura");
        usuario.setTipo("paciente");
        usuario.setConselho("CRM123");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setDataCadastro(LocalDateTime.now());

        assertEquals(1, usuario.getId());
        assertEquals("12345678900", usuario.getCpf());
        assertEquals("João", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("11999999999", usuario.getTelefone());
        assertEquals(70.0, usuario.getPesoInicial());
        assertEquals(1.75, usuario.getAltura());
        assertEquals("cuidador", usuario.getFuncaoResponsavel());
        assertEquals("senhaSegura", usuario.getSenha());
        assertEquals("paciente", usuario.getTipo());
        assertEquals("CRM123", usuario.getConselho());
        assertEquals(LocalDate.of(1990, 1, 1), usuario.getDataNascimento());
        assertNotNull(usuario.getDataCadastro());
    }
}
