package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;


@Schema(description = "Dados básicos do paciente")
public class PacienteResumoDTO {

    @Schema(description = "ID do paciente", example = "3")
    private Integer id;

    @Schema(description = "Nome do paciente", example = "Maria Souza")
    private String nome;

    @Schema(description = "E-mail do paciente", example = "maria.souza@email.com")
    private String email;

    @Schema(description = "CPF do paciente", example = "52998224725")
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


    public String getCpf() {return cpf;}
    public void setCpf(String cpf) {this.cpf = cpf;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    
}