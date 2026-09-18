package br.com.vittasync.vittasync.DTO;

import java.time.LocalDate;
import java.util.List;

/** Resumos calculados uma única vez para a prévia e o documento. */
public record RelatorioResumoDTO(List<Indicador> sinaisVitais, Habitos habitos) {
    public record PontoDiario(LocalDate data, double media) {}

    public record Indicador(String chave, String nome, String unidade,
                            Double media, Double minimo, Double maximo,
                            long quantidade, List<PontoDiario> evolucaoDiaria) {}

    // Sono: média das médias diárias; exercício: soma dos minutos registrados.
    public record Habitos(Double mediaHorasSono, long diasComSono,
                          Long totalMinutosExercicio, long diasComExercicio) {}
}
