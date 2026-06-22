package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.EventoPaciente;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;


class EventoPacienteTest {
    @Test
    void testEventoPaciente() {
        EventoPaciente evento = new EventoPaciente();

        evento.setPacienteId(1);
        evento.setUsuarioId(2);
        evento.setTipoEvento("vinculo_criado");
        evento.setTitulo("Novo participante");
        evento.setDescricao("Usuário entrou no grupo");
        evento.setVisualizado(false);
        evento.setCriadoEm(new Timestamp(System.currentTimeMillis()));
        evento.setPrioridade("normal");

        assertNull(evento.getId());
        assertEquals(1, evento.getPacienteId());
        assertEquals(2, evento.getUsuarioId());
        assertEquals("vinculo_criado", evento.getTipoEvento());
        assertEquals("Novo participante", evento.getTitulo());
        assertEquals("Usuário entrou no grupo", evento.getDescricao());
        assertFalse(evento.getVisualizado());
        assertNotNull(evento.getCriadoEm());
        assertEquals("normal", evento.getPrioridade());
    }
}
