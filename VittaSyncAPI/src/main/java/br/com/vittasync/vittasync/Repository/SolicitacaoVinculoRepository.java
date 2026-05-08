package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.SolicitacaoVinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitacaoVinculoRepository
        extends JpaRepository<SolicitacaoVinculo, Long> {

    boolean existsByCodigo(String codigo);

    Optional<SolicitacaoVinculo> findByCodigo(String codigo);

}