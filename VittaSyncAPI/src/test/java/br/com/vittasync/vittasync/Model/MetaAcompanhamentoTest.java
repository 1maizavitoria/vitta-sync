package br.com.vittasync.vittasync.Model;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class MetaAcompanhamentoTest {
    @Test
    void testMetaAcompanhamento() {
        MetaAcompanhamento meta = new MetaAcompanhamento();
        Usuario paciente = new Usuario();
        LocalDateTime agora = LocalDateTime.now();
        LocalDate limite = LocalDate.now().plusDays(30);

        meta.setId(1L);
        meta.setPaciente(paciente);
        meta.setNome("Perder peso");
        meta.setTipoDado("sinais_vitais");
        meta.setIndicador("peso");
        meta.setDirecao("reduzir");
        meta.setValorInicial(90.0);
        meta.setValorAtual(85.0);
        meta.setUnidade("kg");
        meta.setValorAlvo(80.0);
        meta.setDataLimite(limite);
        meta.setProgresso(50.0);
        meta.setDataCriacao(agora);
        meta.setDataModificacao(agora.plusDays(1));
        meta.setDataConclusao(agora.plusDays(2));
        meta.setStatus("em_andamento");

        assertEquals(1L, meta.getId());
        assertEquals(paciente, meta.getPaciente());
        assertEquals("Perder peso", meta.getNome());
        assertEquals("sinais_vitais", meta.getTipoDado());
        assertEquals("peso", meta.getIndicador());
        assertEquals("reduzir", meta.getDirecao());
        assertEquals(90.0, meta.getValorInicial());
        assertEquals(85.0, meta.getValorAtual());
        assertEquals("kg", meta.getUnidade());
        assertEquals(80.0, meta.getValorAlvo());
        assertEquals(limite, meta.getDataLimite());
        assertEquals(50.0, meta.getProgresso());
        assertEquals(agora, meta.getDataCriacao());
        assertEquals(agora.plusDays(1), meta.getDataModificacao());
        assertEquals(agora.plusDays(2), meta.getDataConclusao());
        assertEquals("em_andamento", meta.getStatus());
    }
}
