package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.ConviteVinculo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConviteVinculoRepository
        extends JpaRepository<ConviteVinculo, Long> {

    boolean existsByCodigo(String codigo);

    Optional<ConviteVinculo> findByCodigo(String codigo);
}