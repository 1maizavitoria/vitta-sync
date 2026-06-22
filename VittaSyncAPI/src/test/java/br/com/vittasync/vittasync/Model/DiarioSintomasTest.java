package br.com.vittasync.vittasync.Model;

import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DiarioSintomasTest {
    @Test
    void testDiarioSintomas() {
        DiarioSintomas diario = new DiarioSintomas();
        Usuario paciente = new Usuario();

        diario.setId(1);
        diario.setPaciente(paciente);
        diario.setSintoma("Dor de cabeça");
        diario.setIntensidadeDor(7);
        diario.setDataReferencia(LocalDate.now());
        diario.setDataRegistro(LocalDateTime.now());
        diario.setDataModificacao(LocalDateTime.now());
        diario.setRegistradoPorUsuarioId(99);

        assertEquals(1, diario.getId());
        assertEquals(paciente, diario.getPaciente());
        assertEquals("Dor de cabeça", diario.getSintoma());
        assertEquals(7, diario.getIntensidadeDor());
        assertNotNull(diario.getDataReferencia());
        assertNotNull(diario.getDataRegistro());
        assertNotNull(diario.getDataModificacao());
        assertEquals(99, diario.getRegistradoPorUsuarioId());
    }
}
