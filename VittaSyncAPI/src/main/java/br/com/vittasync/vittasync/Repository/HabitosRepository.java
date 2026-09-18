package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.Habitos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface HabitosRepository extends JpaRepository<Habitos, Integer> {

    @Query("SELECT h FROM Habitos h WHERE h.paciente.id = :pacienteId "
            + "AND (:inicio IS NULL OR h.dataReferencia >= :inicio) "
            + "AND (:fim IS NULL OR h.dataReferencia <= :fim) "
            + "ORDER BY h.dataReferencia ASC, h.id ASC")
    List<Habitos> buscarParaRelatorio(@Param("pacienteId") Integer pacienteId,
            @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    List<Habitos> findByPacienteCpf(String cpf);

    Optional<Habitos> findFirstByPacienteIdOrderByDataRegistroDesc(Integer pacienteId);

    List<Habitos> findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
            Integer pacienteId,
            LocalDate inicio,
            LocalDate fim
    );

    List<Habitos> findByPacienteId(Integer pacienteId);

    List<Habitos> findByPacienteIdOrderByDataRegistroAsc(Integer pacienteId);

    List<Habitos> findByPacienteIdAndRepousoTrue(Integer pacienteId);

    List<Habitos> findByPacienteIdAndCanal(Integer pacienteId, String canal);

    List<Habitos> findByPacienteIdAndIndiceRepousoLessThan(Integer pacienteId, Double limite);

    List<Habitos> findByPacienteIdAndIndiceRepousoGreaterThanEqual(Integer pacienteId, Double limite);

    List<Habitos> findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
            Integer pacienteId,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}
