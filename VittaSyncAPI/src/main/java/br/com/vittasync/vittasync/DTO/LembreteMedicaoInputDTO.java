package br.com.vittasync.vittasync.DTO;


import java.time.LocalTime;


public class LembreteMedicaoInputDTO {

    private String diasSemana;
    private LocalTime horario;
    private boolean ativo;
    private Boolean enviarEmail;
    private Boolean enviarSms;

    public String getDiasSemana() { return diasSemana; }
    public void setDiasSemana(String diasSemana) { this.diasSemana = diasSemana; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Boolean getEnviarEmail() { return enviarEmail; }
    public void setEnviarEmail(Boolean enviarEmail) { this.enviarEmail = enviarEmail; }

    public Boolean getEnviarSms() { return enviarSms; }
    public void setEnviarSms(Boolean enviarSms) { this.enviarSms = enviarSms; }
}
