package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.Vinculo;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;


class VinculoTest {
    @Test
    void testVinculo() {
        Vinculo vinculo = new Vinculo();

        vinculo.setPacienteId(10);
        vinculo.setUsuarioId(20);
        vinculo.setTipo("responsavel");
        vinculo.setFuncao("cuidador");
        vinculo.setCriadoEm(new Timestamp(System.currentTimeMillis()));

        assertNull(vinculo.getId());
        assertEquals(10, vinculo.getPacienteId());
        assertEquals(20, vinculo.getUsuarioId());
        assertEquals("responsavel", vinculo.getTipo());
        assertEquals("cuidador", vinculo.getFuncao());
        assertNotNull(vinculo.getCriadoEm());
    }
}
