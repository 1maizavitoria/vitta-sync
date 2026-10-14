package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Registro de hábitos do paciente")
public class HabitosOutputDTO {

    @Schema(description = "ID do registro de hábitos", example = "12")
    private Integer id;

    @Schema(description = "Horas de sono no dia", example = "7")
    private Integer horasSono;

    @Schema(description = "Minutos de exercício no dia", example = "30")
    private Integer minutosExercicio;

    @Schema(description = "Dia a que os hábitos se referem", example = "2026-10-13")
    private LocalDate dataReferencia;

    @Schema(description = "Data e hora em que os hábitos foram cadastrados", example = "2026-10-13T20:15:42")
    private LocalDateTime dataRegistro;

    @Schema(description = "Data e hora da última edição, nula se o registro nunca foi editado", example = "2026-10-13T21:02:10")
    private LocalDateTime dataModificacao;

    @Schema(description = "Soma do índice de sono (horas / 8) com o de exercício (minutos / 30, até 1)", example = "1.875")
    private Double indiceRepouso;

    @Schema(description = "Indica necessidade de repouso, verdadeiro quando o índice é menor que 1.5", example = "false")
    private Boolean repouso;

    @Schema(description = "Canal do alerta de repouso", example = "email", allowableValues = {"email", "sms"})
    private String canal;


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

    public Double getIndiceRepouso() { return indiceRepouso; }
    public void setIndiceRepouso(Double indiceRepouso) { this.indiceRepouso = indiceRepouso; }

    public Boolean getRepouso() { return repouso; }
    public void setRepouso(Boolean repouso) { this.repouso = repouso; }

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    
}
