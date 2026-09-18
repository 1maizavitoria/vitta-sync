package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.DiarioSintomas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface DiarioSintomasRepository extends JpaRepository<DiarioSintomas, Integer> {
    @Query("SELECT s FROM DiarioSintomas s WHERE s.paciente.id = :pacienteId "
            + "AND (:inicio IS NULL OR s.dataReferencia >= :inicio) "
            + "AND (:fim IS NULL OR s.dataReferencia <= :fim) "
            + "ORDER BY s.dataReferencia ASC, s.id ASC")
    List<DiarioSintomas> buscarParaRelatorio(@Param("pacienteId") Integer pacienteId,
            @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
    List<DiarioSintomas> findByPacienteCpf(String cpf);
    List<DiarioSintomas> findByPacienteId(Integer pacienteId);
    List<DiarioSintomas> findByPacienteIdOrderByDataRegistroAsc(Integer pacienteId);
    List<DiarioSintomas> findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
            Integer pacienteId,
            LocalDate inicio,
            LocalDate fim
    );

    List<DiarioSintomas> findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
            Integer pacienteId,
            LocalDateTime inicio,
            LocalDateTime fim
    );


}
