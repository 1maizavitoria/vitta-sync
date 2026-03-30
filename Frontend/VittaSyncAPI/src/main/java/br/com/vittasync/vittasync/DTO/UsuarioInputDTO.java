package br.com.vittasync.vittasync.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;


public class UsuarioInputDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 5, message = "Nome deve ter pelo menos 5 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Size(min = 5, message = "Email deve ter pelo menos 5 caracteres")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "Tipo é obrigatório")
    @Pattern(regexp = "paciente|responsavel|saude", message = "Tipo deve ser paciente, responsavel ou saude")
    private String tipo;

    private String conselho;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter exatamente 11 números")
    private String cpf;

    private Boolean privCompartilharDiario;
    private Boolean privCompartilharHabitos;

    @NotNull(message = "Data de nascimento é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

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

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {this.cpf = cpf;}

}
