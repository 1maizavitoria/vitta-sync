package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.SinaisVitais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
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
}
