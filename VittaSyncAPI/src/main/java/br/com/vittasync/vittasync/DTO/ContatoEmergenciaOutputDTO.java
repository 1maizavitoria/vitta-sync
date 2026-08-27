package br.com.vittasync.vittasync.DTO;


import java.time.LocalDateTime;


public class ContatoEmergenciaOutputDTO {

    private Integer id;
    private String nome;
    private String telefone;
    private String email;
    private LocalDateTime dataRegistro;
    private LocalDateTime dataModificacao;
    private Boolean receberAlertaSinaisVitaisSaudavel;
    private Boolean receberAlertaSinaisVitaisModerado;
    private Boolean receberAlertaSinaisVitaisCritico;
    private Boolean receberAlertaHabitosSaudavel;
    private Boolean receberAlertaHabitosModerado;
    private Boolean receberAlertaHabitosCritico;
    private Boolean receberAlertaGeralSaudavel;
    private Boolean receberAlertaGeralModerado;
    private Boolean receberAlertaGeralCritico;
    private Boolean canalEmail;
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
