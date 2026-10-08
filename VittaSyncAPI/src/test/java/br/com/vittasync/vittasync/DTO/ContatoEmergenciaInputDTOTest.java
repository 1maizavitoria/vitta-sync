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
    void testCamposDeAlertaECanais() {
        ContatoEmergenciaInputDTO dto = new ContatoEmergenciaInputDTO();

        assertFalse(dto.getReceberAlertaSinaisVitaisSaudavel());
        assertFalse(dto.getReceberAlertaSinaisVitaisModerado());
        assertTrue(dto.getReceberAlertaSinaisVitaisCritico());
        assertFalse(dto.getReceberAlertaHabitosSaudavel());
        assertTrue(dto.getReceberAlertaHabitosModerado());
        assertTrue(dto.getReceberAlertaHabitosCritico());
        assertFalse(dto.getReceberAlertaGeralSaudavel());
        assertFalse(dto.getReceberAlertaGeralModerado());
        assertTrue(dto.getReceberAlertaGeralCritico());
        assertTrue(dto.getCanalEmail());
        assertFalse(dto.getCanalSms());

        dto.setEmail("maria@teste.com");
        dto.setReceberAlertaSinaisVitaisSaudavel(true);
        dto.setReceberAlertaSinaisVitaisModerado(true);
        dto.setReceberAlertaSinaisVitaisCritico(false);
        dto.setReceberAlertaHabitosSaudavel(true);
        dto.setReceberAlertaHabitosModerado(false);
        dto.setReceberAlertaHabitosCritico(false);
        dto.setReceberAlertaGeralSaudavel(true);
        dto.setReceberAlertaGeralModerado(true);
        dto.setReceberAlertaGeralCritico(false);
        dto.setCanalEmail(false);
        dto.setCanalSms(true);

        assertEquals("maria@teste.com", dto.getEmail());
        assertTrue(dto.getReceberAlertaSinaisVitaisSaudavel());
        assertTrue(dto.getReceberAlertaSinaisVitaisModerado());
        assertFalse(dto.getReceberAlertaSinaisVitaisCritico());
        assertTrue(dto.getReceberAlertaHabitosSaudavel());
        assertFalse(dto.getReceberAlertaHabitosModerado());
        assertFalse(dto.getReceberAlertaHabitosCritico());
        assertTrue(dto.getReceberAlertaGeralSaudavel());
        assertTrue(dto.getReceberAlertaGeralModerado());
        assertFalse(dto.getReceberAlertaGeralCritico());
        assertFalse(dto.getCanalEmail());
        assertTrue(dto.getCanalSms());
    }

    @Test
    void testValidationSuccess() {
        ContatoEmergenciaInputDTO dto = new ContatoEmergenciaInputDTO();
        dto.setNome("Carlos");
        dto.setTelefone("11988887777");
        dto.setEmail("carlos@teste.com");

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
