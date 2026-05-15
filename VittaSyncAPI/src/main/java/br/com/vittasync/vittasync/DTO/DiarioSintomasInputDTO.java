package br.com.vittasync.vittasync.DTO;


import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class DiarioSintomasInputDTO {

    @NotBlank(message = "Sintoma é obrigatório")
    private String sintoma;

    @NotNull(message = "Intensidade da dor é obrigatória")
    @Min(1) @Max(10)
    private Integer intensidadeDor;

    @NotNull(message = "Data de referência é obrigatória")
    private LocalDate dataReferencia;

    private LocalDateTime dataRegistro;
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
