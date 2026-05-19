package br.com.vittasync.vittasync.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class UsuarioOutputDTO {

    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private Double pesoInicial;
    private Double altura;
    private String tipo;
    private String conselho;
    private String funcaoResponsavel;
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

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Double getPesoInicial() { return pesoInicial; }
    public void setPesoInicial(Double pesoInicial) { this.pesoInicial = pesoInicial; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public String getFuncaoResponsavel() { return funcaoResponsavel; }
    public void setFuncaoResponsavel(String funcaoResponsavel) { this.funcaoResponsavel = funcaoResponsavel; }

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
