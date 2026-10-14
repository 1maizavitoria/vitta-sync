package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Dados para enviar o convite por e-mail")
public class EnviarConviteDTO {

    @Schema(description = "E-mail do usuário convidado", example = "carlos.souza@email.com")
    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @Schema(description = "Código de convite gerado pelo paciente", example = "A3F9C1")
    @NotBlank(message = "Código é obrigatório")
    private String codigo;

    
    public EnviarConviteDTO() {}


    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}
    
}