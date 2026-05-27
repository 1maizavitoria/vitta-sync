package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Model.EventoVisualizacao;
import br.com.vittasync.vittasync.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventoVisualizacaoRepository
        extends JpaRepository<EventoVisualizacao, Long> {

    List<EventoVisualizacao>
    findByUsuarioAndVisualizadoFalse(
            Usuario usuario
    );

    Long countByUsuarioAndVisualizadoFalse(
            Usuario usuario
    );

    List<EventoVisualizacao>
    findByEventoPacienteAndVisualizadoFalse(
            EventoPaciente eventoPaciente
    );

    Optional<EventoVisualizacao>
    findByEventoPacienteAndUsuario(
            EventoPaciente eventoPaciente,
            Usuario usuario
    );

    Long countByUsuarioAndVisualizadoFalseAndEventoPacientePacienteId(
            Usuario usuario,
            Integer pacienteId
    );

    List<EventoVisualizacao>
    findByUsuarioAndVisualizadoFalseAndEventoPacientePacienteId(
            Usuario usuario,
            Integer pacienteId
    );
}