package br.com.vittasync.vittasync.DTO;


import java.time.LocalTime;


public class LembreteMedicaoOutputDTO {

    private Long id;
    private String diasSemana;
    private LocalTime horario;
    private boolean ativo;
    private Boolean enviarEmail;
    private Boolean enviarSms;

    public LembreteMedicaoOutputDTO(Long id, String diasSemana, LocalTime horario, boolean ativo, Boolean enviarEmail, Boolean enviarSms) {
        this.id = id;
        this.diasSemana = diasSemana;
        this.horario = horario;
        this.ativo = ativo;
        this.enviarEmail = enviarEmail;
        this.enviarSms = enviarSms;

    }

    public Long getId() { return id; }
    public String getDiasSemana() { return diasSemana; }
    public LocalTime getHorario() { return horario; }
    public boolean isAtivo() { return ativo; }
    public Boolean getEnviarEmail() { return enviarEmail; }
    public Boolean getEnviarSms() { return enviarSms; }
}
