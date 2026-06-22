package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SinaisVitaisInputDTOTest {

    @Test
    void testGettersAndSetters() {
        SinaisVitaisInputDTO dto = new SinaisVitaisInputDTO();

        LocalDateTime agora = LocalDateTime.now();

        dto.setPeso(70.5);
        dto.setFcBpm(72);
        dto.setFrRpm(18);
        dto.setPaSistolica(120);
        dto.setPaDiastolica(80);
        dto.setTempCelcius(36.7);
        dto.setSpo2Porcento(98);
        dto.setDataRegistro(agora);
        dto.setDataModificacao(agora.plusHours(1));

        assertEquals(70.5, dto.getPeso());
        assertEquals(72, dto.getFcBpm());
        assertEquals(18, dto.getFrRpm());
        assertEquals(120, dto.getPaSistolica());
        assertEquals(80, dto.getPaDiastolica());
        assertEquals(36.7, dto.getTempCelcius());
        assertEquals(98, dto.getSpo2Porcento());
        assertEquals(agora, dto.getDataRegistro());
        assertEquals(agora.plusHours(1), dto.getDataModificacao());
    }
}
