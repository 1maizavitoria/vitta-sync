package br.com.vittasync.vittasync.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "PacienteMedico")
public class PacienteMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Integer pacienteId;

    @Column(name = "medico_id")
    private Integer medicoId;

    @Column(name = "criado_em")
    private Timestamp criadoEm;

    public PacienteMedico() {
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

    public Integer getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Integer medicoId) {
        this.medicoId = medicoId;
    }

    public Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }
}