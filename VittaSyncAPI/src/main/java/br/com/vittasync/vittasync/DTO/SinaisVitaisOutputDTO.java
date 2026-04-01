package br.com.vittasync.vittasync.DTO;


import java.time.LocalDateTime;


public class SinaisVitaisOutputDTO {

    private Integer id;
    private Integer fcBpm;
    private Integer frRpm;
    private Integer paSistolica;
    private Integer paDiastolica;
    private Double tempCelcius;
    private Integer spo2Porcento;
    private LocalDateTime dataRegistro;
    private LocalDateTime dataModificacao;

    public SinaisVitaisOutputDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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
