package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;


@Schema(description = "Dados de uma medição de sinais vitais")
public class SinaisVitaisInputDTO {

    @Schema(description = "Peso em quilogramas", example = "72.4")
    @NotNull(message = "Peso é obrigatório")
    private Double peso;

    @Schema(description = "Frequência cardíaca em batimentos por minuto", example = "78")
    @NotNull(message = "Frequência cardíaca é obrigatória")
    private Integer fcBpm;

    @Schema(description = "Frequência respiratória em respirações por minuto", example = "16")
    @NotNull(message = "Frequência respiratória é obrigatória")
    private Integer frRpm;

    @Schema(description = "Pressão arterial sistólica em mmHg", example = "122")
    @NotNull(message = "Pressão sistólica é obrigatória")
    private Integer paSistolica;

    @Schema(description = "Pressão arterial diastólica em mmHg", example = "81")
    @NotNull(message = "Pressão diastólica é obrigatória")
    private Integer paDiastolica;

    @Schema(description = "Temperatura corporal em graus Celsius", example = "36.6")
    @NotNull(message = "Temperatura é obrigatória")
    private Double tempCelcius;

    @Schema(description = "Saturação de oxigênio no sangue em porcentagem", example = "97")
    @NotNull(message = "Saturação O2 é obrigatória")
    private Integer spo2Porcento;


    private LocalDateTime dataRegistro;

    @Schema(hidden = true)
    private LocalDateTime dataModificacao;

    public SinaisVitaisInputDTO() {}


    public Double getPeso() { return peso; }
    public void setPeso(Double peso) {this.peso = peso;}

    public Integer getFcBpm() { return fcBpm; }
    public void setFcBpm(Integer fcBpm) { this.fcBpm = fcBpm; }

    public Integer getFrRpm() { return frRpm; }
    public void setFrRpm(Integer frRpm) { this.frRpm = frRpm; }

    public Integer getPaSistolica() { return paSistolica; }
    public void setPaSistolica(Integer paSistolica) { this.paSistolica = paSistolica; }

    public Integer getPaDiastolica() { return paDiastolica; }
    public void setPaDiastolica(Integer paDiastolica) { this.paDiastolica = paDiastolica; }

    public Double getTempCelcius() { return tempCelcius; }
    public void setTempCelcius(Double tempCelcius) { this.tempCelcius = tempCelcius; }

    public Integer getSpo2Porcento() { return spo2Porcento; }
    public void setSpo2Porcento(Integer spo2Porcento) { this.spo2Porcento = spo2Porcento; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }

}
