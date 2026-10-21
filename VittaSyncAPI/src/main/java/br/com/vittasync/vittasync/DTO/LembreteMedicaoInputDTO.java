package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;


@Schema(description = "Dados do lembrete de medição do paciente")
public class LembreteMedicaoInputDTO {

    @Schema(description = "Dias da semana separados por vírgula", example = "segunda,quarta,sexta")
    private String diasSemana;

    @Schema(description = "Horário do envio (HH:mm:ss)", type = "string", example = "08:00:00")
    private LocalTime horario;

    @Schema(description = "Indica se o lembrete fica ativo", example = "true")
    private boolean ativo;

    @Schema(description = "Enviar o lembrete por e-mail", example = "true")
    private Boolean enviarEmail;

    @Schema(description = "Enviar o lembrete por SMS", example = "false")
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
