package br.com.vittasync.vittasync.Model;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class RelatorioLogTest {
    @Test
    void testRelatorioLog() {
        RelatorioLog log = new RelatorioLog();
        Usuario paciente = new Usuario();
        Usuario usuario = new Usuario();
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        LocalDateTime exportacao = LocalDateTime.now();

        assertNotNull(log.getDataExportacao());

        log.setPaciente(paciente);
        log.setUsuario(usuario);
        log.setFormato("PDF");
        log.setCategorias("SINAIS");
        log.setDataInicio(inicio);
        log.setDataFim(fim);
        log.setDataExportacao(exportacao);

        assertNull(log.getId());
        assertEquals(paciente, log.getPaciente());
        assertEquals(usuario, log.getUsuario());
        assertEquals("PDF", log.getFormato());
        assertEquals("SINAIS", log.getCategorias());
        assertEquals(inicio, log.getDataInicio());
        assertEquals(fim, log.getDataFim());
        assertEquals(exportacao, log.getDataExportacao());
    }
}
