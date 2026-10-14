package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Dados de perfil do usuário")
public class UsuarioOutputDTO {

    @Schema(description = "CPF do usuário (apenas números)", example = "52998224725")
    private String cpf;

    @Schema(description = "Nome completo", example = "Maria Souza")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "maria.souza@email.com")
    private String email;

    @Schema(description = "Telefone com DDD (apenas números)", example = "41987654321")
    private String telefone;

    @Schema(description = "Peso em kg", example = "74.0")
    private Double pesoInicial;

    @Schema(description = "Altura em metros", example = "1.65")
    private Double altura;

    @Schema(description = "Tipo da conta", example = "paciente", allowableValues = {"paciente", "responsavel", "saude"})
    private String tipo;

    @Schema(description = "Registro no conselho profissional, preenchido apenas para o tipo saude", example = "CRM-PR 12345")
    private String conselho;

    @Schema(description = "Função do responsável em relação ao paciente", example = "Cuidadora")
    private String funcaoResponsavel;

    @Schema(description = "Data de nascimento", example = "1958-04-22")
    private LocalDate dataNascimento;

    @Schema(description = "Data e hora do cadastro", example = "2026-09-02T14:10:00")
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

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    
}
