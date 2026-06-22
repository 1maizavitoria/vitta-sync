package br.com.vittasync.vittasync.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SinaisVitaisOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        SinaisVitaisOutputDTO dto = new SinaisVitaisOutputDTO();

        LocalDateTime agora = LocalDateTime.now();

        dto.setId(1);
        dto.setPeso(68.2);
        dto.setFcBpm(70);
        dto.setFrRpm(16);
        dto.setPaSistolica(115);
        dto.setPaDiastolica(75);
        dto.setTempCelcius(36.5);
        dto.setSpo2Porcento(97);
        dto.setDataRegistro(agora);
        dto.setDataModificacao(agora.plusHours(2));

        assertEquals(1, dto.getId());
        assertEquals(68.2, dto.getPeso());
        assertEquals(70, dto.getFcBpm());
        assertEquals(16, dto.getFrRpm());
        assertEquals(115, dto.getPaSistolica());
        assertEquals(75, dto.getPaDiastolica());
        assertEquals(36.5, dto.getTempCelcius());
        assertEquals(97, dto.getSpo2Porcento());
        assertEquals(agora, dto.getDataRegistro());
        assertEquals(agora.plusHours(2), dto.getDataModificacao());
    }
}
