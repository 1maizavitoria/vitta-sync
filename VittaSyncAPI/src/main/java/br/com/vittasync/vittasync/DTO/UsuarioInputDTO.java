package br.com.vittasync.vittasync.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;


@Schema(description = "Dados para cadastrar um usuário")
public class UsuarioInputDTO {

    @Schema(description = "Nome completo, mínimo de 5 caracteres", example = "Maria Souza")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 5, message = "Nome deve ter pelo menos 5 caracteres")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "maria.souza@email.com")
    @NotBlank(message = "Email é obrigatório")
    @Size(min = 5, message = "Email deve ter pelo menos 5 caracteres")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Telefone com DDD (apenas números)", example = "41987654321")
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter DDD e número")
    private String telefone;

    @Schema(description = "Peso em kg", example = "74.0")
    @Positive(message = "Peso inicial deve ser positivo")
    private Double pesoInicial;


    @Schema(description = "Altura em metros", example = "1.65")
    @Positive(message = "Altura deve ser positiva")
    private Double altura;

    @Schema(description = "Função do responsável em relação ao paciente, usado pelo tipo responsavel", example = "Cuidadora")
    private String funcaoResponsavel;

    @Schema(description = "Senha de acesso", example = "Senha@2026")
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @Schema(description = "Tipo da conta", example = "paciente", allowableValues = {"paciente", "responsavel", "saude"})
    @NotBlank(message = "Tipo é obrigatório")
    @Pattern(regexp = "paciente|responsavel|saude", message = "Tipo deve ser paciente, responsavel ou saude")
    private String tipo;

    @Schema(description = "Registro no conselho profissional, obrigatório para o tipo saude", example = "CRM-PR 12345")
    private String conselho;

    @Schema(description = "CPF do usuário (apenas números)", example = "52998224725")
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter exatamente 11 números")
    private String cpf;

    @Schema(description = "Data de nascimento", example = "1958-04-22")
    @NotNull(message = "Data de nascimento é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    
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

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getConselho() { return conselho; }
    public void setConselho(String conselho) { this.conselho = conselho; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {this.cpf = cpf;}

}
