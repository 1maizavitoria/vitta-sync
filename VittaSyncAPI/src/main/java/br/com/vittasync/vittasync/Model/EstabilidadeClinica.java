package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "EstabilidadeClinica")
public class EstabilidadeClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private Integer indice;

    @Column(nullable = false, length = 20)
    private String categoria;

    @Column(nullable = false)
    private Double peso = 1.0; // média ponderada

    @Column(name = "data_calculo", nullable = false)
    private LocalDateTime dataCalculo = LocalDateTime.now();


    public Long getId() {return id;}

    public Integer getPacienteId() {return pacienteId;}

    public void setPacienteId(Integer pacienteId) {this.pacienteId = pacienteId;}

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) {this.tipo = tipo;}

    public Integer getIndice() {return indice;}

    public void setIndice(Integer indice) {this.indice = indice;}

    public String getCategoria() {return categoria;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public Double getPeso() {return peso;}

    public void setPeso(Double peso) {this.peso = peso;}

    public LocalDateTime getDataCalculo() {return dataCalculo;}

    public void setDataCalculo(LocalDateTime dataCalculo) {this.dataCalculo = dataCalculo;}
}
