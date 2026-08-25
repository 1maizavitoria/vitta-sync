package br.com.vittasync.vittasync.DTO;


import java.time.LocalDateTime;


public class EstabilidadeClinicaDTO {

    private String tipo;
    private Integer indice;
    private String categoria;
    private Double peso;
    private LocalDateTime dataCalculo;

    public EstabilidadeClinicaDTO(
            String tipo,
            Integer indice,
            String categoria,
            Double peso,
            LocalDateTime dataCalculo
    ) {
        this.tipo = tipo;
        this.indice = indice;
        this.categoria = categoria;
        this.peso = peso;
        this.dataCalculo = dataCalculo;
    }

    public String getTipo() {return tipo;}

    public Integer getIndice() {return indice;}

    public String getCategoria() {return categoria;}

    public Double getPeso() {return peso;}

    public LocalDateTime getDataCalculo() {return dataCalculo;}
}
