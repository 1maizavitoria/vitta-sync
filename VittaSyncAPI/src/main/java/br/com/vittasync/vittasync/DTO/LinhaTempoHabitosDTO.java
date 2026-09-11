package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;
import java.time.LocalDateTime;


public class LinhaTempoHabitosDTO {
    private Integer id;
    private LocalDateTime dataHora;
    private Integer horasSono;
    private Integer minutosExercicio;
    private Double indiceRepouso;
    private Boolean repouso;
    private String canal;
    private LocalDate dataReferencia;

    public LinhaTempoHabitosDTO(Integer id, LocalDateTime dataHora, Integer horasSono, Integer minutosExercicio,
                                Double indiceRepouso, Boolean repouso, String canal, LocalDate dataReferencia) {
        this.id = id;
        this.dataHora = dataHora;
        this.horasSono = horasSono;
        this.minutosExercicio = minutosExercicio;
        this.indiceRepouso = indiceRepouso;
        this.repouso = repouso;
        this.canal = canal;
        this.dataReferencia = dataReferencia;
    }


    public Integer getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Integer getHorasSono() { return horasSono; }
    public Integer getMinutosExercicio() { return minutosExercicio; }
    public Double getIndiceRepouso() { return indiceRepouso; }
    public Boolean getRepouso() { return repouso; }
    public String getCanal() { return canal; }
    public LocalDate getDataReferencia() { return dataReferencia; }
}
