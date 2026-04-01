package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;
import java.time.LocalDateTime;


public class HabitosOutputDTO {

    private Integer id;
    private Integer horasSono;
    private Integer minutosExercicio;
    private LocalDate dataReferencia;
    private LocalDateTime dataRegistro;
    private LocalDateTime dataModificacao;

    public HabitosOutputDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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
