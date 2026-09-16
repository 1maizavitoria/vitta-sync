package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.RelatorioLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface RelatorioLogRepository extends JpaRepository<RelatorioLog, Long> {

    List<RelatorioLog> findByPacienteId(Integer pacienteId);

    List<RelatorioLog> findByUsuarioId(Integer usuarioId);

    List<RelatorioLog> findByPacienteIdAndDataExportacaoBetween(
            Integer pacienteId,
            java.time.LocalDateTime inicio,
            java.time.LocalDateTime fim
    );

    List<RelatorioLog> findByPacienteIdOrderByDataExportacaoDesc(Integer pacienteId);

}
