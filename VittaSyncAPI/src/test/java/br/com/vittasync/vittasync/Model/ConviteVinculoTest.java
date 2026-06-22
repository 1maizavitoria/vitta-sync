package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.ConviteVinculo;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;


class ConviteVinculoTest {
    @Test
    void testConviteVinculo() {
        ConviteVinculo convite = new ConviteVinculo();

        convite.setPacienteId(10);
        convite.setCodigo("XYZ789");
        convite.setExpiraEm(new Timestamp(System.currentTimeMillis() + 1000));
        convite.setAtivo(true);
        convite.setCriadoEm(new Timestamp(System.currentTimeMillis()));

        assertNull(convite.getId());
        assertEquals(10, convite.getPacienteId());
        assertEquals("XYZ789", convite.getCodigo());
        assertNotNull(convite.getExpiraEm());
        assertTrue(convite.getAtivo());
        assertNotNull(convite.getCriadoEm());
    }
}
