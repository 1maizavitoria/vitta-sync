package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.SinaisVitais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface SinaisVitaisRepository extends JpaRepository<SinaisVitais, Integer> {
    List<SinaisVitais> findByPacienteCpf(String cpf);
    Optional<SinaisVitais> findFirstByPacienteIdOrderByDataRegistroDesc(Integer pacienteId);
    List<SinaisVitais> findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
            Integer pacienteId,
            LocalDateTime inicio,
            LocalDateTime fim
    );
    List<SinaisVitais> findByPacienteIdOrderByDataRegistroAsc(Integer pacienteId);
}
