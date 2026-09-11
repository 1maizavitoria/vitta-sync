package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;
import java.time.LocalDateTime;


public class LinhaTempoSintomasDTO {
    private Integer id;
    private LocalDateTime dataHora;
    private String sintoma;
    private Integer intensidadeDor;
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
