package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.EventoPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface EventoPacienteRepository extends JpaRepository<EventoPaciente, Long> {

    List<EventoPaciente> findByPacienteIdOrderByCriadoEmDesc(Integer pacienteId);

    Long countByPacienteIdAndVisualizadoFalse(Integer pacienteId);

    List<EventoPaciente> findByPacienteIdAndVisualizadoFalse(Integer pacienteId);

    @Query("SELECT new map(e.tipoEvento AS tipo, COUNT(e) AS quantidade) "
            + "FROM EventoPaciente e WHERE e.pacienteId = :pacienteId "
            + "AND e.criadoEm BETWEEN :inicio AND :fim "
            + "GROUP BY e.tipoEvento "
            + "ORDER BY COUNT(e) DESC")
    List<Map<String, Object>> contarPorTipo(@Param("pacienteId") Integer pacienteId,
            @Param("inicio") Timestamp inicio, @Param("fim") Timestamp fim);
}
