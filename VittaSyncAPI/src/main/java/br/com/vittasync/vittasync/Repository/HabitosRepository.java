package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.Habitos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface HabitosRepository extends JpaRepository<Habitos, Integer> {

    List<Habitos> findByPacienteCpf(String cpf);

    Optional<Habitos> findFirstByPacienteIdOrderByDataRegistroDesc(Integer pacienteId);

    List<Habitos> findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
            Integer pacienteId,
            LocalDate inicio,
            LocalDate fim
    );

    List<Habitos> findByPacienteId(Integer pacienteId);
}
