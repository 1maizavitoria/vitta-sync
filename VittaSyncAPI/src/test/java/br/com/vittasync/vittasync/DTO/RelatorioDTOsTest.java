package br.com.vittasync.vittasync.DTO;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class RelatorioDTOsTest {

    @Test
    void testRelatorioPacienteResumoDTO() {
        LocalDate nascimento = LocalDate.of(1985, 5, 20);
        RelatorioPacienteResumoDTO dto = new RelatorioPacienteResumoDTO("Ana", "12345678900", 65.0, 1.68, nascimento);

        assertEquals("Ana", dto.getNome());
        assertEquals("12345678900", dto.getCpf());
        assertEquals(65.0, dto.getPesoInicial());
        assertEquals(1.68, dto.getAltura());
        assertEquals(nascimento, dto.getDataNascimento());
    }

    @Test
    void testRelatorioCSVDTO() {
        byte[] arquivo = "a;b".getBytes();
        RelatorioCSVDTO dto = new RelatorioCSVDTO(arquivo);

        assertArrayEquals(arquivo, dto.getArquivo());
    }

    @Test
    void testRelatorioPDFDTO() {
        byte[] arquivo = new byte[]{1, 2, 3};
        RelatorioPDFDTO dto = new RelatorioPDFDTO(arquivo);

        assertArrayEquals(arquivo, dto.getArquivo());
    }

    @Test
    void testRelatorioResumoDTO() {
        LocalDate hoje = LocalDate.now();
        RelatorioResumoDTO.PontoDiario ponto = new RelatorioResumoDTO.PontoDiario(hoje, 80.0);
        RelatorioResumoDTO.Indicador indicador = new RelatorioResumoDTO.Indicador(
                "fcBpm", "Frequência cardíaca", "bpm", 80.0, 70.0, 90.0, 3, List.of(ponto));
        RelatorioResumoDTO.Habitos habitos = new RelatorioResumoDTO.Habitos(7.5, 2, 60L, 2);

        RelatorioResumoDTO resumo = new RelatorioResumoDTO(List.of(indicador), habitos);

        assertEquals(hoje, ponto.data());
        assertEquals(80.0, ponto.media());
        assertEquals("fcBpm", indicador.chave());
        assertEquals("Frequência cardíaca", indicador.nome());
        assertEquals("bpm", indicador.unidade());
        assertEquals(80.0, indicador.media());
        assertEquals(70.0, indicador.minimo());
        assertEquals(90.0, indicador.maximo());
        assertEquals(3, indicador.quantidade());
        assertEquals(List.of(ponto), indicador.evolucaoDiaria());
        assertEquals(7.5, habitos.mediaHorasSono());
        assertEquals(2, habitos.diasComSono());
        assertEquals(60L, habitos.totalMinutosExercicio());
        assertEquals(2, habitos.diasComExercicio());
        assertEquals(List.of(indicador), resumo.sinaisVitais());
        assertEquals(habitos, resumo.habitos());
    }
}
