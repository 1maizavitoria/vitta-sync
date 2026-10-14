package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Registro da linha do tempo")
public class LinhaTempoItemDTO {

    @Schema(description = "ID do registro original", example = "41")
    private Integer id;

    @Schema(description = "Tipo do registro", example = "SINAL_VITAL", allowableValues = {"SINAL_VITAL", "HABITO", "SINTOMA"})
    private String tipo;

    @Schema(description = "Dia a que o registro se refere", example = "2026-10-13")
    private LocalDate dataReferencia;

    @Schema(description = "Data e hora em que o registro foi cadastrado", example = "2026-10-13T20:15:42")
    private LocalDateTime dataRegistro;

    @Schema(description = "Dados completos do registro, no mesmo formato da lista da categoria correspondente")
    private Object dados;


    public LinhaTempoItemDTO(
            Integer id,
            String tipo,
            LocalDate dataReferencia,
            LocalDateTime dataRegistro,
            Object dados
    ) {
        this.id = id;
        this.tipo = tipo;
        this.dataReferencia = dataReferencia;
        this.dataRegistro = dataRegistro;
        this.dados = dados;
    }

    
    public Integer getId() { return id; }
    
    public String getTipo() { return tipo; }
    
    public LocalDate getDataReferencia() { return dataReferencia; }
    
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    
    public Object getDados() { return dados; }

}
