package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.PacienteResponsavel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteResponsavelRepository
        extends JpaRepository<PacienteResponsavel, Long> {

    boolean existsByPacienteIdAndResponsavelId(
            Integer pacienteId,
            Integer responsavelId
    );
}