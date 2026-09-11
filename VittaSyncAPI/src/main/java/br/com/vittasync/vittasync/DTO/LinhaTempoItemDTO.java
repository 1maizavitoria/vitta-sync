package br.com.vittasync.vittasync.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LinhaTempoItemDTO {

    private Integer id;
    private String tipo;
    private LocalDate dataReferencia;
    private LocalDateTime dataRegistro;
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
