package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Dados de um sintoma relatado pelo paciente")
public class DiarioSintomasInputDTO {

    @Schema(description = "Descrição do sintoma", example = "Dor de cabeça")
    @NotBlank(message = "Sintoma é obrigatório")
    private String sintoma;

    @Schema(description = "Intensidade da dor de 1 a 10", example = "6", minimum = "1", maximum = "10")
    @NotNull(message = "Intensidade da dor é obrigatória")
    @Min(1) @Max(10)
    private Integer intensidadeDor;

    @Schema(description = "Dia em que o sintoma ocorreu", example = "2026-10-13")
    @NotNull(message = "Data de referência é obrigatória")
    private LocalDate dataReferencia;


    private LocalDateTime dataRegistro;

    @Schema(hidden = true)
    private LocalDateTime dataModificacao;

    public DiarioSintomasInputDTO() {}

    
    public String getSintoma() { return sintoma; }
    public void setSintoma(String sintoma) { this.sintoma = sintoma; }

    public Integer getIntensidadeDor() { return intensidadeDor; }
    public void setIntensidadeDor(Integer intensidadeDor) { this.intensidadeDor = intensidadeDor; }

    public LocalDate getDataReferencia() { return dataReferencia; }
    public void setDataReferencia(LocalDate dataReferencia) { this.dataReferencia = dataReferencia; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }
}
