package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.PacienteMedico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteMedicoRepository
        extends JpaRepository<PacienteMedico, Long> {

    boolean existsByPacienteIdAndMedicoId(
            Integer pacienteId,
            Integer medicoId
    );
}