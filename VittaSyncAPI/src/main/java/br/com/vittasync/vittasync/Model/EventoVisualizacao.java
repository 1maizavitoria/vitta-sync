package br.com.vittasync.vittasync.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento_visualizacao")
public class EventoVisualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "evento_id",
            nullable = false
    )
    private EventoPaciente eventoPaciente;

    @ManyToOne
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    @Column(nullable = false)
    private Boolean visualizado = false;

    private LocalDateTime visualizadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventoPaciente getEventoPaciente() {
        return eventoPaciente;
    }

    public void setEventoPaciente(
            EventoPaciente eventoPaciente
    ) {
        this.eventoPaciente = eventoPaciente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(
            Boolean visualizado
    ) {
        this.visualizado = visualizado;
    }

    public LocalDateTime getVisualizadoEm() {
        return visualizadoEm;
    }

    public void setVisualizadoEm(
            LocalDateTime visualizadoEm
    ) {
        this.visualizadoEm = visualizadoEm;
    }
}