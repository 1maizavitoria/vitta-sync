package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;
import java.time.LocalDateTime;


public class LinhaBaseDTO {

    private String sinal;
    private String situacao;
    private Integer diasRegistrados;
    private Integer diasNecessarios;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double media;
    private Double desvioPadrao;
    private Double limiteInferior;
    private Double limiteSuperior;
    private Number ultimoValor;
    private LocalDateTime dataUltimoValor;
    private String comparacao;


    public LinhaBaseDTO(
            String sinal,
            String situacao,
            Integer diasRegistrados,
            Integer diasNecessarios,
            LocalDate dataInicio,
            LocalDate dataFim,
            Double media,
            Double desvioPadrao,
            Double limiteInferior,
            Double limiteSuperior,
            Number ultimoValor,
            LocalDateTime dataUltimoValor,
            String comparacao
    ) {
        this.sinal = sinal;
        this.situacao = situacao;
        this.diasRegistrados = diasRegistrados;
        this.diasNecessarios = diasNecessarios;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.media = media;
        this.desvioPadrao = desvioPadrao;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.ultimoValor = ultimoValor;
        this.dataUltimoValor = dataUltimoValor;
        this.comparacao = comparacao;
    }

    
    public String getSinal() {return sinal;}

    public String getSituacao() {return situacao;}

    public Integer getDiasRegistrados() {return diasRegistrados;}

    public Integer getDiasNecessarios() {return diasNecessarios;}

    public LocalDate getDataInicio() {return dataInicio;}

    public LocalDate getDataFim() {return dataFim;}

    public Double getMedia() {return media;}

    public Double getDesvioPadrao() {return desvioPadrao;}

    public Double getLimiteInferior() {return limiteInferior;}

    public Double getLimiteSuperior() {return limiteSuperior;}

    public Number getUltimoValor() {return ultimoValor;}

    public LocalDateTime getDataUltimoValor() {return dataUltimoValor;}

    public String getComparacao() {return comparacao;}
    
}
