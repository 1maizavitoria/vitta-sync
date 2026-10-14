package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Sintoma na linha do tempo")
public class LinhaTempoSintomasDTO {
    @Schema(description = "ID do sintoma", example = "21")
    private Integer id;

    @Schema(description = "Data e hora em que o registro foi cadastrado", example = "2026-10-13T20:15:42")
    private LocalDateTime dataHora;

    @Schema(description = "Descrição do sintoma", example = "Dor de cabeça")
    private String sintoma;

    @Schema(description = "Intensidade da dor de 1 a 10", example = "6")
    private Integer intensidadeDor;

    @Schema(description = "Dia em que o sintoma ocorreu", example = "2026-10-13")
    private LocalDate dataReferencia;


    public LinhaTempoSintomasDTO(Integer id, LocalDateTime dataHora, String sintoma, Integer intensidadeDor, LocalDate dataReferencia) {
        this.id = id;
        this.dataHora = dataHora;
        this.sintoma = sintoma;
        this.intensidadeDor = intensidadeDor;
        this.dataReferencia = dataReferencia;
    }


    public Integer getId() { return id; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    
    public String getSintoma() { return sintoma; }
    
    public Integer getIntensidadeDor() { return intensidadeDor; }
   
    public LocalDate getDataReferencia() { return dataReferencia; }
    
}
