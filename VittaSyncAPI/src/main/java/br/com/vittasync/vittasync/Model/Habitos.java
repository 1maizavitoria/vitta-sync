package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "Habitos")
public class Habitos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Usuario paciente;

    @Column(name = "horas_sono")
    private Integer horasSono;

    @Column(name = "minutos_exercicio")
    private Integer minutosExercicio;

    @Column(name = "data_referencia")
    private LocalDate dataReferencia;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    public Habitos() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getPaciente() { return paciente; }
    public void setPaciente(Usuario paciente) { this.paciente = paciente; }

    public Integer getHorasSono() { return horasSono; }
    public void setHorasSono(Integer horasSono) { this.horasSono = horasSono; }

    public Integer getMinutosExercicio() { return minutosExercicio; }
    public void setMinutosExercicio(Integer minutosExercicio) { this.minutosExercicio = minutosExercicio; }

    public LocalDate getDataReferencia() { return dataReferencia; }
    public void setDataReferencia(LocalDate dataReferencia) { this.dataReferencia = dataReferencia; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }
}
