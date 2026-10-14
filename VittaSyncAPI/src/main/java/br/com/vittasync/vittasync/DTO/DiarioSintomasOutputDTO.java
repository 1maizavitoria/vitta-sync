package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Sintoma cadastrado no diário do paciente")
public class DiarioSintomasOutputDTO {

    @Schema(description = "ID do sintoma", example = "21")
    private Integer id;

    @Schema(description = "Descrição do sintoma", example = "Dor de cabeça")
    private String sintoma;

    @Schema(description = "Intensidade da dor de 1 a 10", example = "6")
    private Integer intensidadeDor;

    @Schema(description = "Dia em que o sintoma ocorreu", example = "2026-10-13")
    private LocalDate dataReferencia;

    @Schema(description = "Data e hora em que o sintoma foi cadastrado", example = "2026-10-13T20:15:42")
    private LocalDateTime dataRegistro;

    @Schema(description = "Data e hora da última edição, nula se o sintoma nunca foi editado", example = "2026-10-13T21:02:10")
    private LocalDateTime dataModificacao;

    public DiarioSintomasOutputDTO() {}


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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
