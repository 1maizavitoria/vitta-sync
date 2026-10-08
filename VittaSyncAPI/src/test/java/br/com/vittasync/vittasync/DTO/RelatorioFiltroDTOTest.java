package br.com.vittasync.vittasync.DTO;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;


class RelatorioFiltroDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testGettersAndSetters() {
        RelatorioFiltroDTO dto = new RelatorioFiltroDTO();
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);

        assertEquals("PDF", dto.getFormato());
        assertFalse(dto.isPreview());

        dto.setFormato("CSV");
        dto.setCategorias(List.of("SINAIS"));
        dto.setDataInicio(inicio);
        dto.setDataFim(fim);
        dto.setPreview(true);

        assertEquals("CSV", dto.getFormato());
        assertEquals(List.of("SINAIS"), dto.getCategorias());
        assertEquals(inicio, dto.getDataInicio());
        assertEquals(fim, dto.getDataFim());
        assertTrue(dto.isPreview());
    }

    @Test
    void testValidacaoSucesso() {
        RelatorioFiltroDTO dto = new RelatorioFiltroDTO();
        dto.setCategorias(List.of("SINAIS", "HABITOS"));
        dto.setDataInicio(LocalDate.of(2026, 1, 1));
        dto.setDataFim(LocalDate.of(2026, 1, 31));

        Set<ConstraintViolation<RelatorioFiltroDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
        assertTrue(dto.isPeriodoValido());
        assertTrue(dto.isDatasSuportadas());
    }

    @Test
    void testValidacaoSemDatas() {
        RelatorioFiltroDTO dto = new RelatorioFiltroDTO();
        dto.setCategorias(List.of("SINTOMAS"));

        assertTrue(dto.isPeriodoValido());
        assertTrue(dto.isDatasSuportadas());
        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testPeriodoInvalido() {
        RelatorioFiltroDTO dto = new RelatorioFiltroDTO();
        dto.setCategorias(List.of("SINAIS"));
        dto.setDataInicio(LocalDate.of(2026, 2, 1));
        dto.setDataFim(LocalDate.of(2026, 1, 1));

        assertFalse(dto.isPeriodoValido());
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void testDatasNaoSuportadas() {
        RelatorioFiltroDTO dto = new RelatorioFiltroDTO();
        dto.setDataInicio(LocalDate.of(0, 1, 1));
        assertFalse(dto.isDatasSuportadas());

        dto.setDataInicio(null);
        dto.setDataFim(LocalDate.of(10000, 1, 1));
        assertFalse(dto.isDatasSuportadas());
    }

    @Test
    void testFormatoECategoriaInvalidos() {
        RelatorioFiltroDTO dto = new RelatorioFiltroDTO();
        dto.setFormato("XLS");
        dto.setCategorias(List.of("OUTRA"));

        assertEquals(2, validator.validate(dto).size());
    }
}
