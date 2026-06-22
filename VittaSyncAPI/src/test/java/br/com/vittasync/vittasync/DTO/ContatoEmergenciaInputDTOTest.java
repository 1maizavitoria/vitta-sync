package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ContatoEmergenciaInputDTOTest {

    private final Validator validator;

    ContatoEmergenciaInputDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        ContatoEmergenciaInputDTO dto = new ContatoEmergenciaInputDTO();

        dto.setNome("Maria");
        dto.setTelefone("41999999999");

        assertEquals("Maria", dto.getNome());
        assertEquals("41999999999", dto.getTelefone());
    }

    @Test
    void testValidationSuccess() {
        ContatoEmergenciaInputDTO dto = new ContatoEmergenciaInputDTO();
        dto.setNome("Carlos");
        dto.setTelefone("11988887777");

        Set<ConstraintViolation<ContatoEmergenciaInputDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailure() {
        ContatoEmergenciaInputDTO dto = new ContatoEmergenciaInputDTO();
        dto.setNome("");
        dto.setTelefone("123");

        Set<ConstraintViolation<ContatoEmergenciaInputDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
