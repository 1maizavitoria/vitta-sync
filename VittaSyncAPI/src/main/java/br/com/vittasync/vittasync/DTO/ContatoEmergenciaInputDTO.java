package br.com.vittasync.vittasync.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public class ContatoEmergenciaInputDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter DDD e número")
    private String telefone;

    @NotBlank(message = "Email é obrigatório")
    private String email;


    private Boolean receberAlertaSinaisVitaisSaudavel = false;
    private Boolean receberAlertaSinaisVitaisModerado = false;
    private Boolean receberAlertaSinaisVitaisCritico = true;
    private Boolean receberAlertaHabitosSaudavel = false;
    private Boolean receberAlertaHabitosModerado = true;
    private Boolean receberAlertaHabitosCritico = true;
    private Boolean receberAlertaGeralSaudavel = false;
    private Boolean receberAlertaGeralModerado = false;
    private Boolean receberAlertaGeralCritico = true;
    private Boolean canalEmail = true;
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
