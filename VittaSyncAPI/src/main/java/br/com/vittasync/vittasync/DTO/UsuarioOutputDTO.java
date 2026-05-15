package br.com.vittasync.vittasync.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class UsuarioOutputDTO {

    private String cpf;
    private String nome;
    private String email;
    private String tipo;
    private String conselho;
    private Boolean privCompartilharDiario;
    private Boolean privCompartilharHabitos;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastro;


    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getConselho() { return conselho; }
    public void setConselho(String conselho) { this.conselho = conselho; }

    public Boolean getPrivCompartilharDiario() { return privCompartilharDiario; }
    public void setPrivCompartilharDiario(Boolean privCompartilharDiario) { this.privCompartilharDiario = privCompartilharDiario; }

    public Boolean getPrivCompartilharHabitos() { return privCompartilharHabitos; }
    public void setPrivCompartilharHabitos(Boolean privCompartilharHabitos) { this.privCompartilharHabitos = privCompartilharHabitos; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
