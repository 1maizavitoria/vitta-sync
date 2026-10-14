package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;


@Schema(description = "Resumo estatístico do relatório")
public record RelatorioResumoDTO(
        @Schema(description = "Resumo de cada sinal vital no período") List<Indicador> sinaisVitais,
        @Schema(description = "Resumo dos hábitos no período") Habitos habitos) {

    @Schema(description = "Média de um sinal vital em um dia")
    public record PontoDiario(
            @Schema(description = "Dia", example = "2026-10-12") LocalDate data,
            @Schema(description = "Média do dia", example = "78.5") double media) {}

    @Schema(description = "Resumo de um sinal vital no período")
    public record Indicador(
            @Schema(description = "Campo do sinal vital", example = "fcBpm", allowableValues = {"fcBpm", "frRpm", "paSistolica", "paDiastolica", "tempCelcius", "spo2Porcento"}) String chave,
            @Schema(description = "Nome do sinal vital", example = "Frequência cardíaca") String nome,
            @Schema(description = "Unidade de medida", example = "bpm") String unidade,
            @Schema(description = "Média no período", example = "77.4") Double media,
            @Schema(description = "Menor valor no período", example = "68.0") Double minimo,
            @Schema(description = "Maior valor no período", example = "91.0") Double maximo,
            @Schema(description = "Quantidade de medições", example = "12") long quantidade,
            @Schema(description = "Média de cada dia do período") List<PontoDiario> evolucaoDiaria) {}

    @Schema(description = "Resumo dos hábitos no período")
    public record Habitos(
            @Schema(description = "Média de horas de sono", example = "7.2") Double mediaHorasSono,
            @Schema(description = "Dias com sono registrado", example = "10") long diasComSono,
            @Schema(description = "Total de minutos de exercício", example = "240") Long totalMinutosExercicio,
            @Schema(description = "Dias com exercício registrado", example = "8") long diasComExercicio) {}
}
