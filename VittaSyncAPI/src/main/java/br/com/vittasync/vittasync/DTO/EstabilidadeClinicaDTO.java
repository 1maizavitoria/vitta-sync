package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;


@Schema(description = "Índice de estabilidade clínica de um fator ou o índice geral")
public class EstabilidadeClinicaDTO {

    @Schema(description = "Fator avaliado", example = "fc_bpm",
            allowableValues = {"fc_bpm", "fr_rpm", "pressao", "temp_celcius", "spo2", "peso", "sono", "exercicio", "geral"})
    private String tipo;

    @Schema(description = "Índice de 3 (crítico) a 9 (saudável), nulo quando há menos de 3 registros", example = "9")
    private Integer indice;

    @Schema(description = "Classificação do índice", example = "saudavel", allowableValues = {"saudavel", "moderado", "critico", "n/a"})
    private String categoria;

    @Schema(description = "Peso do fator no cálculo do índice geral", example = "1.0")
    private Double peso;

    @Schema(description = "Data e hora do cálculo", example = "2026-10-13T08:42:10")
    private LocalDateTime dataCalculo;

    public EstabilidadeClinicaDTO(
            String tipo,
            Integer indice,
            String categoria,
            Double peso,
            LocalDateTime dataCalculo
    ) {
        this.tipo = tipo;
        this.indice = indice;
        this.categoria = categoria;
        this.peso = peso;
        this.dataCalculo = dataCalculo;
    }

    
    public String getTipo() {return tipo;}

    public Integer getIndice() {return indice;}

    public String getCategoria() {return categoria;}

    public Double getPeso() {return peso;}

    public LocalDateTime getDataCalculo() {return dataCalculo;}
}
