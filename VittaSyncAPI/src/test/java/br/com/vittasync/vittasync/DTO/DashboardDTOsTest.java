package br.com.vittasync.vittasync.DTO;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class DashboardDTOsTest {

    @Test
    void testDashboardPontoDTO() {
        LocalDateTime agora = LocalDateTime.now();
        DashboardPontoDTO dto = new DashboardPontoDTO(agora, 80);

        assertEquals(agora, dto.getData());
        assertEquals(80, dto.getValor());
    }

    @Test
    void testDashboardSerieDTO() {
        DashboardPontoDTO ponto = new DashboardPontoDTO(LocalDateTime.now(), 120);
        DashboardSerieDTO dto = new DashboardSerieDTO("sistolica", "Sistólica", List.of(ponto));

        assertEquals("sistolica", dto.getCodigo());
        assertEquals("Sistólica", dto.getNome());
        assertEquals(List.of(ponto), dto.getPontos());
    }

    @Test
    void testDashboardCategoriaDTO() {
        DashboardSerieDTO serie = new DashboardSerieDTO("peso", "Peso", List.of());
        DashboardCategoriaDTO dto = new DashboardCategoriaDTO("peso", "Peso", "kg", List.of(serie));

        assertEquals("peso", dto.getCodigo());
        assertEquals("Peso", dto.getNome());
        assertEquals("kg", dto.getUnidade());
        assertEquals(List.of(serie), dto.getSeries());
    }

    @Test
    void testDashboardResponseDTO() {
        LocalDate inicio = LocalDate.now().minusDays(6);
        LocalDate fim = LocalDate.now();
        DashboardCategoriaDTO categoria = new DashboardCategoriaDTO("peso", "Peso", "kg", List.of());
        EstabilidadeClinicaDTO estabilidade = new EstabilidadeClinicaDTO("geral", 8, "saudavel", 1.0, LocalDateTime.now());
        LinhaBaseDTO linhaBase = new LinhaBaseDTO("peso", "em_formacao", 3, 14,
                null, null, null, null, null, null, 70.0, LocalDateTime.now(), null);

        DashboardResponseDTO dto = new DashboardResponseDTO("123", "Paciente", inicio, fim,
                List.of(categoria), List.of(estabilidade), List.of(linhaBase));

        assertEquals("123", dto.getPacienteCpf());
        assertEquals("Paciente", dto.getPacienteNome());
        assertEquals(inicio, dto.getInicio());
        assertEquals(fim, dto.getFim());
        assertEquals(List.of(categoria), dto.getCategorias());
        assertEquals(List.of(estabilidade), dto.getEstabilidadeClinica());
        assertEquals(List.of(linhaBase), dto.getLinhasBase());
    }

    @Test
    void testEstabilidadeClinicaDTO() {
        LocalDateTime agora = LocalDateTime.now();
        EstabilidadeClinicaDTO dto = new EstabilidadeClinicaDTO("fc_bpm", 7, "moderado", 1.0, agora);

        assertEquals("fc_bpm", dto.getTipo());
        assertEquals(7, dto.getIndice());
        assertEquals("moderado", dto.getCategoria());
        assertEquals(1.0, dto.getPeso());
        assertEquals(agora, dto.getDataCalculo());
    }

    @Test
    void testLinhaBaseDTO() {
        LocalDate inicio = LocalDate.of(2026, 9, 16);
        LocalDate fim = LocalDate.of(2026, 9, 29);
        LocalDateTime ultimaMedicao = LocalDateTime.now();

        LinhaBaseDTO dto = new LinhaBaseDTO("frequencia_cardiaca", "formada", 14, 14,
                inicio, fim, 71.93, 1.33, 69.27, 74.58, 90, ultimaMedicao, "acima");

        assertEquals("frequencia_cardiaca", dto.getSinal());
        assertEquals("formada", dto.getSituacao());
        assertEquals(14, dto.getDiasRegistrados());
        assertEquals(14, dto.getDiasNecessarios());
        assertEquals(inicio, dto.getDataInicio());
        assertEquals(fim, dto.getDataFim());
        assertEquals(71.93, dto.getMedia());
        assertEquals(1.33, dto.getDesvioPadrao());
        assertEquals(69.27, dto.getLimiteInferior());
        assertEquals(74.58, dto.getLimiteSuperior());
        assertEquals(90, dto.getUltimoValor());
        assertEquals(ultimaMedicao, dto.getDataUltimoValor());
        assertEquals("acima", dto.getComparacao());
    }

    @Test
    void testGerarConviteDTO() {
        GerarConviteDTO dto = new GerarConviteDTO();
        dto.setPacienteId(5);

        assertEquals(5, dto.getPacienteId());
    }
}
