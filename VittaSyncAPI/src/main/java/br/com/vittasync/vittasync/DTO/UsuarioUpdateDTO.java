package br.com.vittasync.vittasync.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import jakarta.validation.constraints.*;


public class UsuarioUpdateDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 5, message = "Nome deve ter pelo menos 5 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Size(min = 5, message = "Email deve ter pelo menos 5 caracteres")
    @Email(message = "Email inválido")
    private String email;

    private Boolean privCompartilharDiario;
    private Boolean privCompartilharHabitos;

    @NotNull(message = "Data de nascimento é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getPrivCompartilharDiario() { return privCompartilharDiario; }
    public void setPrivCompartilharDiario(Boolean privCompartilharDiario) { this.privCompartilharDiario = privCompartilharDiario; }

    public Boolean getPrivCompartilharHabitos() { return privCompartilharHabitos; }
    public void setPrivCompartilharHabitos(Boolean privCompartilharHabitos) { this.privCompartilharHabitos = privCompartilharHabitos; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}
