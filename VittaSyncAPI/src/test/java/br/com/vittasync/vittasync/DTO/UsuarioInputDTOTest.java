package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioInputDTOTest {

    @Test
    void testGettersAndSetters() {
        UsuarioInputDTO dto = new UsuarioInputDTO();

        LocalDate nascimento = LocalDate.of(1990, 5, 20);

        dto.setNome("João da Silva");
        dto.setEmail("joao@exemplo.com");
        dto.setTelefone("41999999999");
        dto.setPesoInicial(75.0);
        dto.setAltura(1.80);
        dto.setFuncaoResponsavel("Responsável Legal");
        dto.setSenha("senhaSegura");
        dto.setTipo("paciente");
        dto.setConselho("CRM12345");
        dto.setCpf("12345678901");
        dto.setDataNascimento(nascimento);

        assertEquals("João da Silva", dto.getNome());
        assertEquals("joao@exemplo.com", dto.getEmail());
        assertEquals("41999999999", dto.getTelefone());
        assertEquals(75.0, dto.getPesoInicial());
        assertEquals(1.80, dto.getAltura());
        assertEquals("Responsável Legal", dto.getFuncaoResponsavel());
        assertEquals("senhaSegura", dto.getSenha());
        assertEquals("paciente", dto.getTipo());
        assertEquals("CRM12345", dto.getConselho());
        assertEquals("12345678901", dto.getCpf());
        assertEquals(nascimento, dto.getDataNascimento());
    }
}
