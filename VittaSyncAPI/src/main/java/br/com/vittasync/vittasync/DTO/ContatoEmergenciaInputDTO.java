package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Schema(description = "Dados de um contato de emergência e das suas preferências de alerta")
public class ContatoEmergenciaInputDTO {

    @Schema(description = "Nome do contato", example = "Carlos Oliveira")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Telefone com DDD (apenas números)", example = "41987654321")
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter DDD e número")
    private String telefone;

    @Schema(description = "E-mail do contato", example = "carlos.oliveira@email.com")
    @NotBlank(message = "Email é obrigatório")
    private String email;


    @Schema(description = "Receber alerta quando os sinais vitais estiverem saudáveis", example = "false")
    private Boolean receberAlertaSinaisVitaisSaudavel = false;

    @Schema(description = "Receber alerta quando os sinais vitais estiverem moderados", example = "false")
    private Boolean receberAlertaSinaisVitaisModerado = false;

    @Schema(description = "Receber alerta quando os sinais vitais estiverem críticos", example = "true")
    private Boolean receberAlertaSinaisVitaisCritico = true;

    @Schema(description = "Receber alerta quando os hábitos estiverem saudáveis", example = "false")
    private Boolean receberAlertaHabitosSaudavel = false;

    @Schema(description = "Receber alerta quando os hábitos estiverem moderados", example = "true")
    private Boolean receberAlertaHabitosModerado = true;

    @Schema(description = "Receber alerta quando os hábitos estiverem críticos", example = "true")
    private Boolean receberAlertaHabitosCritico = true;

    @Schema(description = "Receber alerta quando a estabilidade geral estiver saudável", example = "false")
    private Boolean receberAlertaGeralSaudavel = false;

    @Schema(description = "Receber alerta quando a estabilidade geral estiver moderada", example = "false")
    private Boolean receberAlertaGeralModerado = false;

    @Schema(description = "Receber alerta quando a estabilidade geral estiver crítica", example = "true")
    private Boolean receberAlertaGeralCritico = true;

    @Schema(description = "Enviar os alertas por e-mail", example = "true")
    private Boolean canalEmail = true;

    @Schema(description = "Enviar os alertas por SMS", example = "false")
    private Boolean canalSms = false;


    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getReceberAlertaSinaisVitaisSaudavel() { return receberAlertaSinaisVitaisSaudavel; }
    public void setReceberAlertaSinaisVitaisSaudavel(Boolean receberAlertaSinaisVitaisSaudavel) { this.receberAlertaSinaisVitaisSaudavel = receberAlertaSinaisVitaisSaudavel; }

    public Boolean getReceberAlertaSinaisVitaisModerado() { return receberAlertaSinaisVitaisModerado; }
    public void setReceberAlertaSinaisVitaisModerado(Boolean receberAlertaSinaisVitaisModerado) { this.receberAlertaSinaisVitaisModerado = receberAlertaSinaisVitaisModerado; }

    public Boolean getReceberAlertaSinaisVitaisCritico() { return receberAlertaSinaisVitaisCritico; }
    public void setReceberAlertaSinaisVitaisCritico(Boolean receberAlertaSinaisVitaisCritico) { this.receberAlertaSinaisVitaisCritico = receberAlertaSinaisVitaisCritico; }

    public Boolean getReceberAlertaHabitosSaudavel() { return receberAlertaHabitosSaudavel; }
    public void setReceberAlertaHabitosSaudavel(Boolean receberAlertaHabitosSaudavel) { this.receberAlertaHabitosSaudavel = receberAlertaHabitosSaudavel; }

    public Boolean getReceberAlertaHabitosModerado() { return receberAlertaHabitosModerado; }
    public void setReceberAlertaHabitosModerado(Boolean receberAlertaHabitosModerado) { this.receberAlertaHabitosModerado = receberAlertaHabitosModerado; }

    public Boolean getReceberAlertaHabitosCritico() { return receberAlertaHabitosCritico; }
    public void setReceberAlertaHabitosCritico(Boolean receberAlertaHabitosCritico) { this.receberAlertaHabitosCritico = receberAlertaHabitosCritico; }

    public Boolean getReceberAlertaGeralSaudavel() { return receberAlertaGeralSaudavel; }
    public void setReceberAlertaGeralSaudavel(Boolean receberAlertaGeralSaudavel) { this.receberAlertaGeralSaudavel = receberAlertaGeralSaudavel; }

    public Boolean getReceberAlertaGeralModerado() { return receberAlertaGeralModerado; }
    public void setReceberAlertaGeralModerado(Boolean receberAlertaGeralModerado) { this.receberAlertaGeralModerado = receberAlertaGeralModerado; }

    public Boolean getReceberAlertaGeralCritico() { return receberAlertaGeralCritico; }
    public void setReceberAlertaGeralCritico(Boolean receberAlertaGeralCritico) { this.receberAlertaGeralCritico = receberAlertaGeralCritico; }

    public Boolean getCanalEmail() { return canalEmail; }
    public void setCanalEmail(Boolean canalEmail) { this.canalEmail = canalEmail; }

    public Boolean getCanalSms() { return canalSms; }
    public void setCanalSms(Boolean canalSms) { this.canalSms = canalSms; }
}
