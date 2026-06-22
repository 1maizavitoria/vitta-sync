package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

class EventoPacienteOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        EventoPacienteOutputDTO dto = new EventoPacienteOutputDTO();

        Timestamp agora = new Timestamp(System.currentTimeMillis());

        dto.setId(100L);
        dto.setPacienteId(10);
        dto.setUsuarioId(20);
        dto.setTipoEvento("Consulta");
        dto.setTitulo("Consulta marcada");
        dto.setDescricao("Paciente tem consulta amanhã");
        dto.setVisualizado(true);
        dto.setCriadoEm(agora);
        dto.setPrioridade("Alta");
        dto.setUsuarioNome("Dr. Silva");
        dto.setUsuarioTipo("Médico");

        assertEquals(100L, dto.getId());
        assertEquals(10, dto.getPacienteId());
        assertEquals(20, dto.getUsuarioId());
        assertEquals("Consulta", dto.getTipoEvento());
        assertEquals("Consulta marcada", dto.getTitulo());
        assertEquals("Paciente tem consulta amanhã", dto.getDescricao());
        assertTrue(dto.getVisualizado());
        assertEquals(agora, dto.getCriadoEm());
        assertEquals("Alta", dto.getPrioridade());
        assertEquals("Dr. Silva", dto.getUsuarioNome());
        assertEquals("Médico", dto.getUsuarioTipo());
    }
}
