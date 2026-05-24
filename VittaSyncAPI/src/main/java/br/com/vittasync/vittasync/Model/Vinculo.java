package br.com.vittasync.vittasync.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Vinculo")
public class Vinculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Integer pacienteId;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "funcao")
    private String funcao;

    @Column(name = "criado_em")
    private Timestamp criadoEm;

    public Vinculo() {
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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}