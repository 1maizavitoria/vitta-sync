package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.SinaisVitais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface SinaisVitaisRepository extends JpaRepository<SinaisVitais, Integer> {

    @Query("SELECT s FROM SinaisVitais s WHERE s.paciente.id = :pacienteId "
            + "AND (:inicio IS NULL OR s.dataRegistro >= :inicio) "
            + "AND (:fimExclusivo IS NULL OR s.dataRegistro < :fimExclusivo) "
            + "ORDER BY s.dataRegistro ASC, s.id ASC")
    List<SinaisVitais> buscarParaRelatorio(@Param("pacienteId") Integer pacienteId,
            @Param("inicio") LocalDateTime inicio, @Param("fimExclusivo") LocalDateTime fimExclusivo);

    List<SinaisVitais> findByPacienteCpf(String cpf);

    Optional<SinaisVitais> findFirstByPacienteIdOrderByDataRegistroDesc(Integer pacienteId);

    List<SinaisVitais> findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
            Integer pacienteId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<SinaisVitais> findByPacienteIdOrderByDataRegistroAsc(Integer pacienteId);

    List<SinaisVitais> findByPacienteId(Integer pacienteId);

    @Query("SELECT DISTINCT s.paciente.id FROM SinaisVitais s")
    List<Integer> findTodosPacientesIds();

    @Query("SELECT new map(CAST(s.dataRegistro AS LocalDate) AS dia, "
            + "ROUND(AVG(s.peso), 1) AS mediaPeso, ROUND(AVG(s.fcBpm), 1) AS mediaFcBpm, "
            + "ROUND(AVG(s.frRpm), 1) AS mediaFrRpm, ROUND(AVG(s.paSistolica), 1) AS mediaPaSistolica, "
            + "ROUND(AVG(s.paDiastolica), 1) AS mediaPaDiastolica, ROUND(AVG(s.tempCelcius), 1) AS mediaTempCelcius, "
            + "ROUND(AVG(s.spo2Porcento), 1) AS mediaSpo2Porcento, COUNT(s) AS medicoes) "
            + "FROM SinaisVitais s WHERE s.paciente.id = :pacienteId "
            + "AND s.dataRegistro BETWEEN :inicio AND :fim "
            + "GROUP BY CAST(s.dataRegistro AS LocalDate) "
            + "ORDER BY CAST(s.dataRegistro AS LocalDate)")
    List<Map<String, Object>> buscarResumoDiario(@Param("pacienteId") Integer pacienteId,
            @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
