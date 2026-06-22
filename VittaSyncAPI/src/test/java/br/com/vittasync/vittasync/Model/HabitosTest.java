package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class HabitosTest {
    @Test
    void testHabitos() {
        Habitos habito = new Habitos();
        Usuario paciente = new Usuario();

        habito.setId(1);
        habito.setPaciente(paciente);
        habito.setHorasSono(8);
        habito.setMinutosExercicio(30);
        habito.setDataReferencia(LocalDate.now());
        habito.setDataRegistro(LocalDateTime.now());
        habito.setDataModificacao(LocalDateTime.now());
        habito.setRegistradoPorUsuarioId(99);

        assertEquals(1, habito.getId());
        assertEquals(paciente, habito.getPaciente());
        assertEquals(8, habito.getHorasSono());
        assertEquals(30, habito.getMinutosExercicio());
        assertNotNull(habito.getDataReferencia());
        assertNotNull(habito.getDataRegistro());
        assertNotNull(habito.getDataModificacao());
        assertEquals(99, habito.getRegistradoPorUsuarioId());
    }
}
