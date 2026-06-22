package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.EventoVisualizacao;
import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class EventoVisualizacaoTest {
    @Test
    void testEventoVisualizacao() {
        EventoVisualizacao ev = new EventoVisualizacao();
        EventoPaciente evento = new EventoPaciente();
        Usuario usuario = new Usuario();

        ev.setId(1L);
        ev.setEventoPaciente(evento);
        ev.setUsuario(usuario);
        ev.setVisualizado(true);
        ev.setVisualizadoEm(LocalDateTime.now());

        assertEquals(1L, ev.getId());
        assertEquals(evento, ev.getEventoPaciente());
        assertEquals(usuario, ev.getUsuario());
        assertTrue(ev.getVisualizado());
        assertNotNull(ev.getVisualizadoEm());
    }
}
