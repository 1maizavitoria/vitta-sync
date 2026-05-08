package br.com.vittasync.vittasync.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "PacienteResponsavel")
public class PacienteResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Integer pacienteId;

    @Column(name = "responsavel_id")
    private Integer responsavelId;

    @Column(name = "criado_em")
    private Timestamp criadoEm;

    public PacienteResponsavel() {
    }

    public Long getId() {
        return id;
    }

    public Integer getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Integer pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Integer getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Integer responsavelId) {
        this.responsavelId = responsavelId;
    }

    public Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }
}