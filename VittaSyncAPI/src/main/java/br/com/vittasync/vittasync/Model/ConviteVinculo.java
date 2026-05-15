package br.com.vittasync.vittasync.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "ConviteVinculo")
public class ConviteVinculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Integer pacienteId;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "expira_em")
    private Timestamp expiraEm;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "criado_em")
    private Timestamp criadoEm;

    public ConviteVinculo() {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Timestamp getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Timestamp expiraEm) {
        this.expiraEm = expiraEm;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }
}