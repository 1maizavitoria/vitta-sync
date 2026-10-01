package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.LinhaBase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface LinhaBaseRepository extends JpaRepository<LinhaBase, Integer> {

    List<LinhaBase> findByPacienteId(Integer pacienteId);

    Optional<LinhaBase> findByPacienteIdAndSinal(Integer pacienteId, String sinal);

    boolean existsByPacienteIdAndSinal(Integer pacienteId, String sinal);
}
