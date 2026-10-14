package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Dados para solicitar a redefinição de senha")
public class SolicitarRedefinicaoDTO {

    @Schema(description = "E-mail cadastrado do usuário", example = "maria.oliveira@email.com")
    private String email;

    @Schema(description = "Canal de envio do código de redefinição", example = "email", allowableValues = {"email", "sms"})
    private String canal;


    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getCanal() {return canal;}
    public void setCanal(String canal) {this.canal = canal;}

}
