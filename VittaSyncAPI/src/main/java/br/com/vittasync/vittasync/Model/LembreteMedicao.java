package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalTime;


@Entity
@Table(name = "LembreteMedicao")
public class LembreteMedicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "dias_semana", nullable = false)
    private String diasSemana; // "segunda,terca,quarta,quinta,sexta,sabado,domingo"

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private boolean ativo = true;


    public Long getId() { return id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getDiasSemana() { return diasSemana; }
    public void setDiasSemana(String diasSemana) { this.diasSemana = diasSemana; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
