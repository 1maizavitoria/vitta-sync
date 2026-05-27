package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.EventoPaciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoPacienteRepository extends JpaRepository<EventoPaciente, Long> {

    List<EventoPaciente> findByPacienteIdOrderByCriadoEmDesc(Integer pacienteId);

    Long countByPacienteIdAndVisualizadoFalse(Integer pacienteId);

    List<EventoPaciente> findByPacienteIdAndVisualizadoFalse(Integer pacienteId);
}