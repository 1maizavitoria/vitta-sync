package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;


@Schema(description = "Vínculo entre o paciente e um responsável ou profissional de saúde")
public class VinculoOutputDTO {

    @Schema(description = "ID do vínculo", example = "6")
    private Long id;

    @Schema(description = "Nome do outro usuário do vínculo", example = "Carlos Souza")
    private String nome;

    @Schema(description = "E-mail do outro usuário do vínculo", example = "carlos.souza@email.com")
    private String email;

    @Schema(description = "Tipo do usuário vinculado ao paciente", example = "responsavel", allowableValues = {"responsavel", "saude"})
    private String tipo;

    @Schema(description = "Registro no conselho profissional, preenchido apenas para profissionais de saúde", example = "CRM-PR 12345")
    private String conselho;

    @Schema(description = "Função do usuário vinculado em relação ao paciente", example = "cuidador")
    private String funcao;

    @Schema(description = "Data e hora em que o vínculo foi criado", example = "2026-10-01T10:20:00")
    private Timestamp criadoEm;


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    public String getConselho() {return conselho;}
    public void setConselho(String conselho) {this.conselho = conselho;}

    public Timestamp getCriadoEm() {return criadoEm;}
    public void setCriadoEm(Timestamp criadoEm) {this.criadoEm = criadoEm;}

    public String getFuncao() {return funcao;}
    public void setFuncao(String funcao) {this.funcao = funcao;}
    
}