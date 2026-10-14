package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Hábitos na linha do tempo")
public class LinhaTempoHabitosDTO {
    @Schema(description = "ID do registro de hábitos", example = "12")
    private Integer id;

    @Schema(description = "Data e hora em que o registro foi cadastrado", example = "2026-10-13T20:15:42")
    private LocalDateTime dataHora;

    @Schema(description = "Horas de sono no dia", example = "7")
    private Integer horasSono;

    @Schema(description = "Minutos de exercício no dia", example = "30")
    private Integer minutosExercicio;

    @Schema(description = "Índice de repouso calculado", example = "1.875")
    private Double indiceRepouso;

    @Schema(description = "Indica necessidade de repouso", example = "false")
    private Boolean repouso;

    @Schema(description = "Canal do alerta de repouso", example = "email", allowableValues = {"email", "sms"})
    private String canal;

    @Schema(description = "Dia a que os hábitos se referem", example = "2026-10-13")
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


    public Integer getId() {return id;}
    
    public LocalDateTime getDataHora() {return dataHora;}
   
    public Integer getHorasSono() { return horasSono; }
    
    public Integer getMinutosExercicio() {return minutosExercicio;}
   
    public Double getIndiceRepouso() {return indiceRepouso;}
   
    public Boolean getRepouso() {return repouso;}
   
    public String getCanal() {return canal;}
   
    public LocalDate getDataReferencia() {return dataReferencia;}
    
}
