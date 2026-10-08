package br.com.vittasync.vittasync.DTO;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class RelatorioPreviewDTOTest {

    private RelatorioPacienteResumoDTO paciente() {
        return new RelatorioPacienteResumoDTO("Paciente", "123", 70.0, 1.75, LocalDate.of(1990, 1, 1));
    }

    @Test
    void testSemRegistros() {
        RelatorioPreviewDTO dto = new RelatorioPreviewDTO(paciente(), List.of(), List.of(), List.of());

        assertTrue(dto.isSemRegistros());
        assertEquals("Nenhum registro encontrado para os filtros selecionados.", dto.getMensagem());
    }

    @Test
    void testComRegistros() {
        LinhaTempoSinaisVitaisDTO sinal = new LinhaTempoSinaisVitaisDTO(1, LocalDateTime.now(), 70.0, 80, 16, 120, 80, 36.5, 98);
        RelatorioPreviewDTO dto = new RelatorioPreviewDTO(paciente(), List.of(sinal), List.of(), List.of());

        assertFalse(dto.isSemRegistros());
        assertNull(dto.getMensagem());
    }

    @Test
    void testGettersAndSetters() {
        RelatorioPreviewDTO dto = new RelatorioPreviewDTO(paciente(), List.of(), List.of(), List.of());

        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        LocalDateTime emissao = LocalDateTime.now();
        RelatorioPacienteResumoDTO outroPaciente = new RelatorioPacienteResumoDTO("Outro", "456", null, null, null);
        LinhaTempoSinaisVitaisDTO sinal = new LinhaTempoSinaisVitaisDTO(1, emissao, 70.0, 80, 16, 120, 80, 36.5, 98);
        LinhaTempoHabitosDTO habito = new LinhaTempoHabitosDTO(2, emissao, 8, 30, 1.0, false, "app", inicio);
        LinhaTempoSintomasDTO sintoma = new LinhaTempoSintomasDTO(3, emissao, "Dor", 4, inicio);
        RelatorioResumoDTO resumo = new RelatorioResumoDTO(List.of(), new RelatorioResumoDTO.Habitos(8.0, 1, 30L, 1));
        List<String> categorias = new ArrayList<>(List.of("SINAIS", "HABITOS"));

        dto.setDataInicio(inicio);
        dto.setDataFim(fim);
        dto.setDataEmissao(emissao);
        dto.setCategorias(categorias);
        dto.setResumo(resumo);
        dto.setPaciente(outroPaciente);
        dto.setSinaisVitais(List.of(sinal));
        dto.setHabitos(List.of(habito));
        dto.setSintomas(List.of(sintoma));

        assertEquals(inicio, dto.getDataInicio());
        assertEquals(fim, dto.getDataFim());
        assertEquals(emissao, dto.getDataEmissao());
        assertEquals(List.of("SINAIS", "HABITOS"), dto.getCategorias());
        assertThrows(UnsupportedOperationException.class, () -> dto.getCategorias().add("SINTOMAS"));
        assertEquals(resumo, dto.getResumo());
        assertEquals(outroPaciente, dto.getPaciente());
        assertEquals(1, dto.getSinaisVitais().size());
        assertEquals(1, dto.getHabitos().size());
        assertEquals(1, dto.getSintomas().size());
    }
}
