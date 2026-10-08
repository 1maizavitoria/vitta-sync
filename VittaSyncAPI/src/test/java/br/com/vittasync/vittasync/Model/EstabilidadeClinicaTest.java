package br.com.vittasync.vittasync.Model;


import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class EstabilidadeClinicaTest {
    @Test
    void testEstabilidadeClinica() {
        EstabilidadeClinica estabilidade = new EstabilidadeClinica();
        LocalDateTime calculo = LocalDateTime.now();

        assertEquals(1.0, estabilidade.getPeso());
        assertNotNull(estabilidade.getDataCalculo());

        estabilidade.setPacienteId(2);
        estabilidade.setTipo("geral");
        estabilidade.setIndice(7);
        estabilidade.setCategoria("moderado");
        estabilidade.setPeso(2.0);
        estabilidade.setDataCalculo(calculo);

        assertNull(estabilidade.getId());
        assertEquals(2, estabilidade.getPacienteId());
        assertEquals("geral", estabilidade.getTipo());
        assertEquals(7, estabilidade.getIndice());
        assertEquals("moderado", estabilidade.getCategoria());
        assertEquals(2.0, estabilidade.getPeso());
        assertEquals(calculo, estabilidade.getDataCalculo());
    }
}
