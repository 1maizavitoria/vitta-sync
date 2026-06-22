package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class ContatoEmergenciaTest {
    @Test
    void testContatoEmergencia() {
        ContatoEmergencia contato = new ContatoEmergencia();
        Usuario paciente = new Usuario();

        contato.setId(1);
        contato.setPaciente(paciente);
        contato.setNome("Maria");
        contato.setTelefone("11999999999");
        contato.setDataRegistro(LocalDateTime.now());
        contato.setDataModificacao(LocalDateTime.now());

        assertEquals(1, contato.getId());
        assertEquals(paciente, contato.getPaciente());
        assertEquals("Maria", contato.getNome());
        assertEquals("11999999999", contato.getTelefone());
        assertNotNull(contato.getDataRegistro());
        assertNotNull(contato.getDataModificacao());
    }
}
