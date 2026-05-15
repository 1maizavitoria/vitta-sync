package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.Vinculo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VinculoRepository
        extends JpaRepository<Vinculo, Long> {

    boolean existsByPacienteIdAndUsuarioId(
            Integer pacienteId,
            Integer usuarioId
    );

    boolean existsByPacienteIdAndUsuarioIdAndTipo(
            Integer pacienteId,
            Integer usuarioId,
            String tipo
    );

    List<Vinculo> findByPacienteId(
            Integer pacienteId
    );

    List<Vinculo> findByUsuarioId(
            Integer usuarioId
    );


}