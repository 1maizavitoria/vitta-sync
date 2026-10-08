package br.com.vittasync.vittasync.DTO;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


class MetaAcompanhamentoInputDTOTest {

    @Test
    void testGettersAndSetters() {
        MetaAcompanhamentoInputDTO dto = new MetaAcompanhamentoInputDTO();

        LocalDate limite = LocalDate.now().plusDays(15);

        dto.setNome("Dormir mais");
        dto.setTipoDado("habitos");
        dto.setIndicador("horas_sono");
        dto.setDirecao("aumentar");
        dto.setValorInicial(5.0);
        dto.setValorAtual(6.0);
        dto.setUnidade("horas");
        dto.setValorAlvo(8.0);
        dto.setDataLimite(limite);

        assertEquals("Dormir mais", dto.getNome());
        assertEquals("habitos", dto.getTipoDado());
        assertEquals("horas_sono", dto.getIndicador());
        assertEquals("aumentar", dto.getDirecao());
        assertEquals(5.0, dto.getValorInicial());
        assertEquals(6.0, dto.getValorAtual());
        assertEquals("horas", dto.getUnidade());
        assertEquals(8.0, dto.getValorAlvo());
        assertEquals(limite, dto.getDataLimite());
    }
}
