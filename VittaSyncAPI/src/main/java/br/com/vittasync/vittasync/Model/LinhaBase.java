package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "LinhaBase",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_linha_base_paciente_sinal",
                columnNames = {"paciente_id", "sinal"}
        )
)
public class LinhaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "sinal", nullable = false, length = 30)
    private String sinal;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "media", nullable = false)
    private Double media;

    @Column(name = "desvio_padrao", nullable = false)
    private Double desvioPadrao;

    // nulos quando o desvio padrão é zero: sem faixa, o sinal não é comparado
    @Column(name = "limite_inferior")
    private Double limiteInferior;

    @Column(name = "limite_superior")
    private Double limiteSuperior;

    @Column(name = "data_formacao", nullable = false)
    private LocalDateTime dataFormacao;

    public LinhaBase() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPacienteId() { return pacienteId; }
    public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }

    public String getSinal() { return sinal; }
    public void setSinal(String sinal) { this.sinal = sinal; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public Double getMedia() { return media; }
    public void setMedia(Double media) { this.media = media; }

    public Double getDesvioPadrao() { return desvioPadrao; }
    public void setDesvioPadrao(Double desvioPadrao) { this.desvioPadrao = desvioPadrao; }

    public Double getLimiteInferior() { return limiteInferior; }
    public void setLimiteInferior(Double limiteInferior) { this.limiteInferior = limiteInferior; }

    public Double getLimiteSuperior() { return limiteSuperior; }
    public void setLimiteSuperior(Double limiteSuperior) { this.limiteSuperior = limiteSuperior; }

    public LocalDateTime getDataFormacao() { return dataFormacao; }
    public void setDataFormacao(LocalDateTime dataFormacao) { this.dataFormacao = dataFormacao; }
}
