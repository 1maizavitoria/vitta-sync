package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.EstabilidadeClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface EstabilidadeClinicaRepository extends JpaRepository<EstabilidadeClinica, Long> {

    List<EstabilidadeClinica> findByPacienteIdOrderByDataCalculoDesc(Integer pacienteId);

    List<EstabilidadeClinica> findByPacienteIdAndTipoOrderByDataCalculoDesc(Integer pacienteId, String tipo);

    EstabilidadeClinica findTopByPacienteIdAndTipoOrderByDataCalculoDesc(Integer pacienteId, String tipo);

    default String findUltimaCategoriaGeral(Integer pacienteId) {
        EstabilidadeClinica ultima = findTopByPacienteIdAndTipoOrderByDataCalculoDesc(pacienteId, "geral");
        return ultima != null ? ultima.getCategoria() : null;
    }

    @Query(value = "CALL sp_medias_estabilidade(:pacienteId)", nativeQuery = true)
    List<Object[]> buscarMediasEstabilidade(@Param("pacienteId") Integer pacienteId);
}
