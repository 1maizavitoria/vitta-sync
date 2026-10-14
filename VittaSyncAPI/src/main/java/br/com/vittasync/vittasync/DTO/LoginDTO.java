package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Dados para iniciar o login")
public class LoginDTO {

    @Schema(description = "CPF do usuário (apenas números)", example = "52998224725")
    private String cpf;

    @Schema(description = "Senha do usuário", example = "Senha@2026")
    private String senha;

    @Schema(description = "Canal de envio do código de verificação", example = "email", allowableValues = {"email", "sms"})
    private String canal;


    public String getCpf() {return cpf;}
    public void setCpf(String cpf) {this.cpf = cpf;}

    public String getSenha() { return senha; }
    public void setSenha(String senha) {this.senha = senha;}

    public String getCanal() { return canal; }
    public void setCanal(String canal) {this.canal = canal;}
}
