package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiarioSintomasInputDTOTest {

    private final Validator validator;

    DiarioSintomasInputDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        DiarioSintomasInputDTO dto = new DiarioSintomasInputDTO();

        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();

        dto.setSintoma("Dor de cabeça");
        dto.setIntensidadeDor(5);
        dto.setDataReferencia(hoje);
        dto.setDataRegistro(agora);
        dto.setDataModificacao(agora.plusHours(1));

        assertEquals("Dor de cabeça", dto.getSintoma());
        assertEquals(5, dto.getIntensidadeDor());
        assertEquals(hoje, dto.getDataReferencia());
        assertEquals(agora, dto.getDataRegistro());
        assertEquals(agora.plusHours(1), dto.getDataModificacao());
    }

    @Test
    void testValidationSuccess() {
        DiarioSintomasInputDTO dto = new DiarioSintomasInputDTO();
        dto.setSintoma("Náusea");
        dto.setIntensidadeDor(7);
        dto.setDataReferencia(LocalDate.now());

        Set<ConstraintViolation<DiarioSintomasInputDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailure() {
        DiarioSintomasInputDTO dto = new DiarioSintomasInputDTO();
        dto.setSintoma("");
        dto.setIntensidadeDor(15);
        dto.setDataReferencia(null);

        Set<ConstraintViolation<DiarioSintomasInputDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
