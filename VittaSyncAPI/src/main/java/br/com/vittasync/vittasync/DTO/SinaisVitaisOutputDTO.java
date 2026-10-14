package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;


@Schema(description = "Medição de sinais vitais registrada")
public class SinaisVitaisOutputDTO {

    @Schema(description = "Id da medição", example = "15")
    private Integer id;

    @Schema(description = "Peso em quilogramas", example = "72.4")
    private Double peso;

    @Schema(description = "Frequência cardíaca em batimentos por minuto", example = "78")
    private Integer fcBpm;

    @Schema(description = "Frequência respiratória em respirações por minuto", example = "16")
    private Integer frRpm;

    @Schema(description = "Pressão arterial sistólica em mmHg", example = "122")
    private Integer paSistolica;

    @Schema(description = "Pressão arterial diastólica em mmHg", example = "81")
    private Integer paDiastolica;

    @Schema(description = "Temperatura corporal em graus Celsius", example = "36.6")
    private Double tempCelcius;

    @Schema(description = "Saturação de oxigênio no sangue em porcentagem", example = "97")
    private Integer spo2Porcento;

    @Schema(description = "Data e hora em que a medição foi registrada", example = "2026-10-13T08:42:10")
    private LocalDateTime dataRegistro;

    @Schema(description = "Data e hora da última edição, nula se a medição nunca foi editada", example = "2026-10-13T09:05:33")
    private LocalDateTime dataModificacao;

    public SinaisVitaisOutputDTO() {}


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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
