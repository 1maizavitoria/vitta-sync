package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;


@Schema(description = "Contato de emergência cadastrado e suas preferências de alerta")
public class ContatoEmergenciaOutputDTO {

    @Schema(description = "ID do contato de emergência", example = "3")
    private Integer id;

    @Schema(description = "Nome do contato", example = "Carlos Oliveira")
    private String nome;

    @Schema(description = "Telefone com DDD", example = "41987654321")
    private String telefone;

    @Schema(description = "E-mail do contato", example = "carlos.oliveira@email.com")
    private String email;

    @Schema(description = "Data e hora em que o contato foi cadastrado", example = "2026-10-05T10:15:00")
    private LocalDateTime dataRegistro;

    @Schema(description = "Data e hora da última edição, nula se o contato nunca foi editado", example = "2026-10-12T18:40:21")
    private LocalDateTime dataModificacao;

    @Schema(description = "Recebe alerta quando os sinais vitais estiverem saudáveis", example = "false")
    private Boolean receberAlertaSinaisVitaisSaudavel;

    @Schema(description = "Recebe alerta quando os sinais vitais estiverem moderados", example = "false")
    private Boolean receberAlertaSinaisVitaisModerado;

    @Schema(description = "Recebe alerta quando os sinais vitais estiverem críticos", example = "true")
    private Boolean receberAlertaSinaisVitaisCritico;

    @Schema(description = "Recebe alerta quando os hábitos estiverem saudáveis", example = "false")
    private Boolean receberAlertaHabitosSaudavel;

    @Schema(description = "Recebe alerta quando os hábitos estiverem moderados", example = "true")
    private Boolean receberAlertaHabitosModerado;

    @Schema(description = "Recebe alerta quando os hábitos estiverem críticos", example = "true")
    private Boolean receberAlertaHabitosCritico;

    @Schema(description = "Recebe alerta quando a estabilidade geral estiver saudável", example = "false")
    private Boolean receberAlertaGeralSaudavel;

    @Schema(description = "Recebe alerta quando a estabilidade geral estiver moderada", example = "false")
    private Boolean receberAlertaGeralModerado;

    @Schema(description = "Recebe alerta quando a estabilidade geral estiver crítica", example = "true")
    private Boolean receberAlertaGeralCritico;

    @Schema(description = "Alertas enviados por e-mail", example = "true")
    private Boolean canalEmail;

    @Schema(description = "Alertas enviados por SMS", example = "false")
    private Boolean canalSms;


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }

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
