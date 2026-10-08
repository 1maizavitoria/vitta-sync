package br.com.vittasync.vittasync.DTO;


import br.com.vittasync.vittasync.Model.RelatorioLog;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class RelatorioLogDTOTest {

    @Test
    void testConstrutorComUsuarioEPaciente() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Responsável");

        Usuario paciente = new Usuario();
        paciente.setId(2);
        paciente.setNome("Paciente");

        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        LocalDateTime exportacao = LocalDateTime.now();

        RelatorioLog log = new RelatorioLog();
        log.setUsuario(usuario);
        log.setPaciente(paciente);
        log.setFormato("PDF");
        log.setCategorias("SINAIS,HABITOS");
        log.setDataInicio(inicio);
        log.setDataFim(fim);
        log.setDataExportacao(exportacao);

        RelatorioLogDTO dto = new RelatorioLogDTO(log);

        assertNull(dto.getId());
        assertEquals("PDF", dto.getFormato());
        assertEquals("SINAIS,HABITOS", dto.getCategorias());
        assertEquals(inicio, dto.getDataInicio());
        assertEquals(fim, dto.getDataFim());
        assertEquals(exportacao, dto.getDataExportacao());
        assertEquals(1, dto.getUsuarioId());
        assertEquals("Responsável", dto.getUsuarioNome());
        assertEquals(2, dto.getPacienteId());
        assertEquals("Paciente", dto.getPacienteNome());
    }

    @Test
    void testConstrutorSemUsuarioEPaciente() {
        RelatorioLog log = new RelatorioLog();
        log.setFormato("CSV");

        RelatorioLogDTO dto = new RelatorioLogDTO(log);

        assertEquals("CSV", dto.getFormato());
        assertNull(dto.getUsuarioId());
        assertNull(dto.getUsuarioNome());
        assertNull(dto.getPacienteId());
        assertNull(dto.getPacienteNome());
    }

    @Test
    void testSetters() {
        RelatorioLogDTO dto = new RelatorioLogDTO(new RelatorioLog());
        LocalDate inicio = LocalDate.of(2026, 2, 1);
        LocalDate fim = LocalDate.of(2026, 2, 28);
        LocalDateTime exportacao = LocalDateTime.now();

        dto.setId(10);
        dto.setFormato("CSV");
        dto.setCategorias("SINTOMAS");
        dto.setDataInicio(inicio);
        dto.setDataFim(fim);
        dto.setDataExportacao(exportacao);
        dto.setUsuarioId(3);
        dto.setUsuarioNome("Médico");
        dto.setPacienteId(4);
        dto.setPacienteNome("Paciente 2");

        assertEquals(10, dto.getId());
        assertEquals("CSV", dto.getFormato());
        assertEquals("SINTOMAS", dto.getCategorias());
        assertEquals(inicio, dto.getDataInicio());
        assertEquals(fim, dto.getDataFim());
        assertEquals(exportacao, dto.getDataExportacao());
        assertEquals(3, dto.getUsuarioId());
        assertEquals("Médico", dto.getUsuarioNome());
        assertEquals(4, dto.getPacienteId());
        assertEquals("Paciente 2", dto.getPacienteNome());
    }
}
