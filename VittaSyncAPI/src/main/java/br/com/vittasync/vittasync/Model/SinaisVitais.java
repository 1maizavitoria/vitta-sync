package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "SinaisVitais")
public class SinaisVitais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Usuario paciente;

    @Column(name = "fc_bpm")
    private Integer fcBpm;

    @Column(name = "fr_rpm")
    private Integer frRpm;

    @Column(name = "pa_sistolica")
    private Integer paSistolica;

    @Column(name = "pa_diastolica")
    private Integer paDiastolica;

    @Column(name = "temp_celcius")
    private Double tempCelcius;

    @Column(name = "spo2_porcento")
    private Integer spo2Porcento;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    public SinaisVitais() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getPaciente() { return paciente; }
    public void setPaciente(Usuario paciente) { this.paciente = paciente; }

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
