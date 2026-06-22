package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;


class LembreteMedicaoTest {
    @Test
    void testLembreteMedicao() {
        LembreteMedicao lembrete = new LembreteMedicao();
        Usuario usuario = new Usuario();

        lembrete.setUsuario(usuario);
        lembrete.setDiasSemana("segunda,terca,quarta");
        lembrete.setHorario(LocalTime.of(8, 0));
        lembrete.setAtivo(true);
        lembrete.setEnviarEmail(true);
        lembrete.setEnviarSms(false);

        assertNull(lembrete.getId());
        assertEquals(usuario, lembrete.getUsuario());
        assertEquals("segunda,terca,quarta", lembrete.getDiasSemana());
        assertEquals(LocalTime.of(8, 0), lembrete.getHorario());
        assertTrue(lembrete.isAtivo());
        assertTrue(lembrete.getEnviarEmail());
        assertFalse(lembrete.getEnviarSms());
    }
}
