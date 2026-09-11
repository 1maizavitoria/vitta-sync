package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.DiarioSintomas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;


public interface DiarioSintomasRepository extends JpaRepository<DiarioSintomas, Integer> {
    List<DiarioSintomas> findByPacienteCpf(String cpf);
    List<DiarioSintomas> findByPacienteId(Integer pacienteId);
    List<DiarioSintomas> findByPacienteIdOrderByDataRegistroAsc(Integer pacienteId);
    List<DiarioSintomas> findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
            Integer pacienteId,
            LocalDate inicio,
            LocalDate fim
    );
}
