package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "ContatoEmergencia")
public class ContatoEmergencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Usuario paciente;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 15)
    private String telefone;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "receber_alerta_sinais_vitais_saudavel")
    private Boolean receberAlertaSinaisVitaisSaudavel = false;

    @Column(name = "receber_alerta_sinais_vitais_moderado")
    private Boolean receberAlertaSinaisVitaisModerado = false;

    @Column(name = "receber_alerta_sinais_vitais_critico")
    private Boolean receberAlertaSinaisVitaisCritico = true;

    @Column(name = "receber_alerta_habitos_saudavel")
    private Boolean receberAlertaHabitosSaudavel = false;

    @Column(name = "receber_alerta_habitos_moderado")
    private Boolean receberAlertaHabitosModerado = true;

    @Column(name = "receber_alerta_habitos_critico")
    private Boolean receberAlertaHabitosCritico = true;

    @Column(name = "receber_alerta_geral_saudavel")
    private Boolean receberAlertaGeralSaudavel = false;

    @Column(name = "receber_alerta_geral_moderado")
    private Boolean receberAlertaGeralModerado = false;

    @Column(name = "receber_alerta_geral_critico")
    private Boolean receberAlertaGeralCritico = true;

    @Column(name = "canal_email")
    private Boolean canalEmail = true;

    @Column(name = "canal_sms")
    private Boolean canalSms = false;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    public ContatoEmergencia() {}


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getPaciente() { return paciente; }
    public void setPaciente(Usuario paciente) { this.paciente = paciente; }

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

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }
}
