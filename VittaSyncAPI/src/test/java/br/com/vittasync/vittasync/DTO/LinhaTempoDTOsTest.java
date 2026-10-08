package br.com.vittasync.vittasync.DTO;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class LinhaTempoDTOsTest {

    @Test
    void testLinhaTempoSinaisVitaisDTO() {
        LocalDateTime agora = LocalDateTime.now();
        LinhaTempoSinaisVitaisDTO dto = new LinhaTempoSinaisVitaisDTO(1, agora, 72.5, 80, 18, 120, 80, 36.7, 97);

        assertEquals(1, dto.getId());
        assertEquals(agora, dto.getDataHora());
        assertEquals(72.5, dto.getPeso());
        assertEquals(80, dto.getFcBpm());
        assertEquals(18, dto.getFrRpm());
        assertEquals(120, dto.getPaSistolica());
        assertEquals(80, dto.getPaDiastolica());
        assertEquals(36.7, dto.getTempCelcius());
        assertEquals(97, dto.getSpo2Porcento());
    }

    @Test
    void testLinhaTempoHabitosDTO() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = LocalDate.now();
        LinhaTempoHabitosDTO dto = new LinhaTempoHabitosDTO(2, agora, 7, 45, 0.8, true, "app", hoje);

        assertEquals(2, dto.getId());
        assertEquals(agora, dto.getDataHora());
        assertEquals(7, dto.getHorasSono());
        assertEquals(45, dto.getMinutosExercicio());
        assertEquals(0.8, dto.getIndiceRepouso());
        assertTrue(dto.getRepouso());
        assertEquals("app", dto.getCanal());
        assertEquals(hoje, dto.getDataReferencia());
    }

    @Test
    void testLinhaTempoSintomasDTO() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = LocalDate.now();
        LinhaTempoSintomasDTO dto = new LinhaTempoSintomasDTO(3, agora, "Dor de cabeça", 6, hoje);

        assertEquals(3, dto.getId());
        assertEquals(agora, dto.getDataHora());
        assertEquals("Dor de cabeça", dto.getSintoma());
        assertEquals(6, dto.getIntensidadeDor());
        assertEquals(hoje, dto.getDataReferencia());
    }

    @Test
    void testLinhaTempoItemDTO() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = LocalDate.now();
        LinhaTempoItemDTO dto = new LinhaTempoItemDTO(4, "SINTOMA", hoje, agora, "dados");

        assertEquals(4, dto.getId());
        assertEquals("SINTOMA", dto.getTipo());
        assertEquals(hoje, dto.getDataReferencia());
        assertEquals(agora, dto.getDataRegistro());
        assertEquals("dados", dto.getDados());
    }

    @Test
    void testLinhaTempoResponseDTO() {
        LocalDateTime agora = LocalDateTime.now();
        LinhaTempoSinaisVitaisDTO sinal = new LinhaTempoSinaisVitaisDTO(1, agora, 70.0, 80, 16, 120, 80, 36.5, 98);
        LinhaTempoHabitosDTO habito = new LinhaTempoHabitosDTO(2, agora, 8, 30, 1.0, false, "app", LocalDate.now());
        LinhaTempoSintomasDTO sintoma = new LinhaTempoSintomasDTO(3, agora, "Náusea", 2, LocalDate.now());
        LinhaTempoItemDTO item = new LinhaTempoItemDTO(1, "SINAIS_VITAIS", LocalDate.now(), agora, sinal);

        LinhaTempoResponseDTO dto = new LinhaTempoResponseDTO(List.of(sinal), List.of(habito), List.of(sintoma), List.of(item));

        assertEquals(List.of(sinal), dto.getSinaisVitais());
        assertEquals(List.of(habito), dto.getHabitos());
        assertEquals(List.of(sintoma), dto.getSintomas());
        assertEquals(List.of(item), dto.getItens());
    }
}
