package br.com.vittasync.vittasync.DTO;


import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class HabitosInputDTO {

    @NotNull(message = "Horas de sono são obrigatórias")
    private Integer horasSono;

    @NotNull(message = "Minutos de exercício são obrigatórios")
    private Integer minutosExercicio;

    @NotNull(message = "Data de referência é obrigatória")
    private LocalDate dataReferencia;

    private LocalDateTime dataRegistro;
    private LocalDateTime dataModificacao;

    public HabitosInputDTO() {}

    public Integer getHorasSono() { return horasSono; }
    public void setHorasSono(Integer horasSono) { this.horasSono = horasSono; }

    public Integer getMinutosExercicio() { return minutosExercicio; }
    public void setMinutosExercicio(Integer minutosExercicio) { this.minutosExercicio = minutosExercicio; }

    public LocalDate getDataReferencia() { return dataReferencia; }
    public void setDataReferencia(LocalDate dataReferencia) { this.dataReferencia = dataReferencia; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }
}
