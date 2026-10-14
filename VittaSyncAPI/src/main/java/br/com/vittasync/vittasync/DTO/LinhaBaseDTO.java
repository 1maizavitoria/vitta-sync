package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Linha de base personalizada de um sinal vital, formada após 14 dias seguidos com registro")
public class LinhaBaseDTO {

    @Schema(description = "Código do sinal vital", example = "frequencia_cardiaca",
            allowableValues = {"peso", "frequencia_cardiaca", "frequencia_respiratoria", "pressao_sistolica",
                    "pressao_diastolica", "temperatura", "saturacao"})
    private String sinal;

    @Schema(description = "Situação da referência", example = "formada", allowableValues = {"em_formacao", "formada", "sem_variacao"})
    private String situacao;

    @Schema(description = "Dias seguidos com registro, de 0 a 14", example = "14")
    private Integer diasRegistrados;

    @Schema(description = "Dias seguidos necessários para formar a referência", example = "14")
    private Integer diasNecessarios;

    @Schema(description = "Primeiro dia do período usado na referência, nulo se ainda em formação", example = "2026-09-16")
    private LocalDate dataInicio;

    @Schema(description = "Último dia do período usado na referência, nulo se ainda em formação", example = "2026-09-29")
    private LocalDate dataFim;

    @Schema(description = "Média das 14 médias diárias", example = "71.93")
    private Double media;

    @Schema(description = "Desvio padrão amostral das 14 médias diárias", example = "1.33")
    private Double desvioPadrao;

    @Schema(description = "Limite inferior da faixa (média menos dois desvios padrão), nulo sem variação", example = "69.27")
    private Double limiteInferior;

    @Schema(description = "Limite superior da faixa (média mais dois desvios padrão), nulo sem variação", example = "74.58")
    private Double limiteSuperior;

    @Schema(description = "Valor da medição mais recente do sinal", example = "90")
    private Number ultimoValor;

    @Schema(description = "Data e hora da medição mais recente do sinal", example = "2026-10-13T08:42:10")
    private LocalDateTime dataUltimoValor;

    @Schema(description = "Posição da medição mais recente em relação à faixa, nula se a medição formou a referência ou se não há faixa",
            example = "acima", allowableValues = {"abaixo", "dentro", "acima"})
    private String comparacao;


    public LinhaBaseDTO(
            String sinal,
            String situacao,
            Integer diasRegistrados,
            Integer diasNecessarios,
            LocalDate dataInicio,
            LocalDate dataFim,
            Double media,
            Double desvioPadrao,
            Double limiteInferior,
            Double limiteSuperior,
            Number ultimoValor,
            LocalDateTime dataUltimoValor,
            String comparacao
    ) {
        this.sinal = sinal;
        this.situacao = situacao;
        this.diasRegistrados = diasRegistrados;
        this.diasNecessarios = diasNecessarios;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.media = media;
        this.desvioPadrao = desvioPadrao;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.ultimoValor = ultimoValor;
        this.dataUltimoValor = dataUltimoValor;
        this.comparacao = comparacao;
    }

    
    public String getSinal() {return sinal;}

    public String getSituacao() {return situacao;}

    public Integer getDiasRegistrados() {return diasRegistrados;}

    public Integer getDiasNecessarios() {return diasNecessarios;}

    public LocalDate getDataInicio() {return dataInicio;}

    public LocalDate getDataFim() {return dataFim;}

    public Double getMedia() {return media;}

    public Double getDesvioPadrao() {return desvioPadrao;}

    public Double getLimiteInferior() {return limiteInferior;}

    public Double getLimiteSuperior() {return limiteSuperior;}

    public Number getUltimoValor() {return ultimoValor;}

    public LocalDateTime getDataUltimoValor() {return dataUltimoValor;}

    public String getComparacao() {return comparacao;}
    
}
