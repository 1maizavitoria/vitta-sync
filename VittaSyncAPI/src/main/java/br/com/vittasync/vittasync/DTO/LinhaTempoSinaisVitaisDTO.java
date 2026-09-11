package br.com.vittasync.vittasync.DTO;


import java.time.LocalDateTime;


public class LinhaTempoSinaisVitaisDTO {
    private Integer id;
    private LocalDateTime dataHora;
    private Double peso;
    private Integer fcBpm;
    private Integer frRpm;
    private Integer paSistolica;
    private Integer paDiastolica;
    private Double tempCelcius;
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

    public Integer getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Double getPeso() { return peso; }
    public Integer getFcBpm() { return fcBpm; }
    public Integer getFrRpm() { return frRpm; }
    public Integer getPaSistolica() { return paSistolica; }
    public Integer getPaDiastolica() { return paDiastolica; }
    public Double getTempCelcius() { return tempCelcius; }
    public Integer getSpo2Porcento() { return spo2Porcento; }
}
