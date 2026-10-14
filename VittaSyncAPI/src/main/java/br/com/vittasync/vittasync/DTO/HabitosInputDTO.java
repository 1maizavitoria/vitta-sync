package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;


@Schema(description = "Dados dos hábitos do paciente em um dia")
public class HabitosInputDTO {

    @Schema(description = "Horas de sono no dia", example = "7")
    @NotNull(message = "Horas de sono são obrigatórias")
    private Integer horasSono;

    @Schema(description = "Minutos de exercício no dia", example = "30")
    @NotNull(message = "Minutos de exercício são obrigatórios")
    private Integer minutosExercicio;

    @Schema(description = "Dia a que os hábitos se referem", example = "2026-10-13")
    @NotNull(message = "Data de referência é obrigatória")
    private LocalDate dataReferencia;

    @Schema(description = "Canal do alerta de repouso para os contatos de emergência, e-mail quando não informado", example = "email", allowableValues = {"email", "sms"})
    private String canal;

    public HabitosInputDTO() {}


    public Integer getHorasSono() { return horasSono;}
    public void setHorasSono(Integer horasSono) { this.horasSono = horasSono;}

    public Integer getMinutosExercicio() { return minutosExercicio; }
    public void setMinutosExercicio(Integer minutosExercicio) { this.minutosExercicio = minutosExercicio;}

    public LocalDate getDataReferencia() { return dataReferencia; }
    public void setDataReferencia(LocalDate dataReferencia) { this.dataReferencia = dataReferencia;}

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal;}

}
