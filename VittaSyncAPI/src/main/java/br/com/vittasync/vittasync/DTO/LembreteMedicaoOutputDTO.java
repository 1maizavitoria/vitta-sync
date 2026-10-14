package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;


@Schema(description = "Lembrete de medição do paciente")
public class LembreteMedicaoOutputDTO {

    @Schema(description = "ID do lembrete", example = "4")
    private Long id;

    @Schema(description = "Dias da semana em inglês separados por vírgula", example = "MONDAY,WEDNESDAY,FRIDAY")
    private String diasSemana;

    @Schema(description = "Horário do envio (HH:mm:ss)", type = "string", example = "08:00:00")
    private LocalTime horario;

    @Schema(description = "Indica se o lembrete está ativo", example = "true")
    private boolean ativo;

    @Schema(description = "Enviar o lembrete por e-mail", example = "true")
    private Boolean enviarEmail;

    @Schema(description = "Enviar o lembrete por SMS", example = "false")
    private Boolean enviarSms;


    public LembreteMedicaoOutputDTO(Long id, String diasSemana, LocalTime horario, boolean ativo, Boolean enviarEmail, Boolean enviarSms) {
        this.id = id;
        this.diasSemana = diasSemana;
        this.horario = horario;
        this.ativo = ativo;
        this.enviarEmail = enviarEmail;
        this.enviarSms = enviarSms;

    }


    public Long getId() {return id;}
    
    public String getDiasSemana() {return diasSemana;}
    
    public LocalTime getHorario() {return horario;}
    
    public boolean isAtivo() {return ativo;}
    
    public Boolean getEnviarEmail() {return enviarEmail;}
    
    public Boolean getEnviarSms() {return enviarSms;}
}
