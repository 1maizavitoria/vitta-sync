package br.com.vittasync.vittasync.DTO;

import java.sql.Timestamp;

public class PacienteResumoDTO {

    private Integer id;
    private String nome;
    private String email;
    private String cpf;

    public PacienteResumoDTO() {
    }

    public PacienteResumoDTO(
            Integer id,
            String nome,
            String email,
            String cpf
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}