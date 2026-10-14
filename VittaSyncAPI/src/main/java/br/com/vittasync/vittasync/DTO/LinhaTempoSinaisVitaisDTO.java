package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;


@Schema(description = "Sinais vitais na linha do tempo")
public class LinhaTempoSinaisVitaisDTO {
    @Schema(description = "ID da medição", example = "41")
    private Integer id;

    @Schema(description = "Data e hora em que o registro foi cadastrado", example = "2026-10-13T20:15:42")
    private LocalDateTime dataHora;

    @Schema(description = "Peso em kg", example = "72.5")
    private Double peso;

    @Schema(description = "Frequência cardíaca em bpm", example = "78")
    private Integer fcBpm;

    @Schema(description = "Frequência respiratória em rpm", example = "16")
    private Integer frRpm;

    @Schema(description = "Pressão arterial sistólica em mmHg", example = "120")
    private Integer paSistolica;

    @Schema(description = "Pressão arterial diastólica em mmHg", example = "80")
    private Integer paDiastolica;

    @Schema(description = "Temperatura corporal em °C", example = "36.7")
    private Double tempCelcius;

    @Schema(description = "Saturação de oxigênio em %", example = "98")
    private Integer spo2Porcento;



    public LinhaTempoSinaisVitaisDTO(Integer id, LocalDateTime dataHora, Double peso, Integer fcBpm,
                                     Integer frRpm, Integer paSistolica, Integer paDiastolica,
                                     Double tempCelcius, Integer spo2Porcento) {
        this.id = id;
        this.dataHora = dataHora;
        this.peso = peso;
        this.fcBpm = fcBpm;
        this.frRpm = frRpm;
        this.paSistolica = paSistolica;
        this.paDiastolica = paDiastolica;
        this.tempCelcius = tempCelcius;
        this.spo2Porcento = spo2Porcento;
    }


    public Integer getId() {return id;}
    
    public LocalDateTime getDataHora() {return dataHora;}
   
    public Double getPeso() {return peso;}
    
    public Integer getFcBpm() {return fcBpm;}
    
    public Integer getFrRpm() {return frRpm;}
   
    public Integer getPaSistolica() {return paSistolica;}
   
    public Integer getPaDiastolica() {return paDiastolica;}
   
    public Double getTempCelcius() {return tempCelcius;}
   
    public Integer getSpo2Porcento() {return spo2Porcento;}

}
