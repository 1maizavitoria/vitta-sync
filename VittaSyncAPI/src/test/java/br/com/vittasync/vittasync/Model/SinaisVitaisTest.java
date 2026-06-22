package br.com.vittasync.vittasync.Model;


import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class SinaisVitaisTest {
    @Test
    void testSinaisVitais() {
        SinaisVitais sinais = new SinaisVitais();
        Usuario paciente = new Usuario();

        sinais.setId(1);
        sinais.setPaciente(paciente);
        sinais.setPeso(70.5);
        sinais.setFcBpm(80);
        sinais.setFrRpm(18);
        sinais.setPaSistolica(120);
        sinais.setPaDiastolica(80);
        sinais.setTempCelcius(36.7);
        sinais.setSpo2Porcento(98);
        sinais.setDataRegistro(LocalDateTime.now());
        sinais.setDataModificacao(LocalDateTime.now());
        sinais.setRegistradoPorUsuarioId(99);

        assertEquals(1, sinais.getId());
        assertEquals(paciente, sinais.getPaciente());
        assertEquals(70.5, sinais.getPeso());
        assertEquals(80, sinais.getFcBpm());
        assertEquals(18, sinais.getFrRpm());
        assertEquals(120, sinais.getPaSistolica());
        assertEquals(80, sinais.getPaDiastolica());
        assertEquals(36.7, sinais.getTempCelcius());
        assertEquals(98, sinais.getSpo2Porcento());
        assertNotNull(sinais.getDataRegistro());
        assertNotNull(sinais.getDataModificacao());
        assertEquals(99, sinais.getRegistradoPorUsuarioId());
    }
}
