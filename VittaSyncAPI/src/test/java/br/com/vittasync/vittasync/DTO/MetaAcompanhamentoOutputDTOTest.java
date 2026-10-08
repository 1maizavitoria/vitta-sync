package br.com.vittasync.vittasync.DTO;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class MetaAcompanhamentoOutputDTOTest {

    @Test
    void testGettersAndSetters() {
        MetaAcompanhamentoOutputDTO dto = new MetaAcompanhamentoOutputDTO();

        LocalDateTime agora = LocalDateTime.now();
        LocalDate limite = LocalDate.now().plusDays(30);

        dto.setId(1L);
        dto.setNome("Perder peso");
        dto.setTipoDado("sinais_vitais");
        dto.setIndicador("peso");
        dto.setDirecao("reduzir");
        dto.setValorInicial(90.0);
        dto.setValorAtual(85.0);
        dto.setUnidade("kg");
        dto.setValorAlvo(80.0);
        dto.setProgresso(50.0);
        dto.setStatus("em_andamento");
        dto.setDataCriacao(agora);
        dto.setDataLimite(limite);
        dto.setDataConclusao(agora.plusDays(1));
        dto.setDataModificacao(agora.plusDays(2));

        assertEquals(1L, dto.getId());
        assertEquals("Perder peso", dto.getNome());
        assertEquals("sinais_vitais", dto.getTipoDado());
        assertEquals("peso", dto.getIndicador());
        assertEquals("reduzir", dto.getDirecao());
        assertEquals(90.0, dto.getValorInicial());
        assertEquals(85.0, dto.getValorAtual());
        assertEquals("kg", dto.getUnidade());
        assertEquals(80.0, dto.getValorAlvo());
        assertEquals(50.0, dto.getProgresso());
        assertEquals("em_andamento", dto.getStatus());
        assertEquals(agora, dto.getDataCriacao());
        assertEquals(limite, dto.getDataLimite());
        assertEquals(agora.plusDays(1), dto.getDataConclusao());
        assertEquals(agora.plusDays(2), dto.getDataModificacao());
    }
}
