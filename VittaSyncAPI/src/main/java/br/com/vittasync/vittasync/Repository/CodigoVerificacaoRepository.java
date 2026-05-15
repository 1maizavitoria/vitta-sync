package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface CodigoVerificacaoRepository extends JpaRepository<CodigoVerificacao, Integer> {

    @Query("""
           SELECT c FROM CodigoVerificacao c
           WHERE c.codigo = :codigo
           AND c.tipo = :tipo
           AND c.utilizado = false
           AND c.expira > CURRENT_TIMESTAMP
           """)
    Optional<CodigoVerificacao> findCodigoValido(
            @Param("codigo") String codigo,
            @Param("tipo") String tipo
    );

    List<CodigoVerificacao> findByUsuarioIdAndTipoAndUtilizadoFalse(Integer usuarioId, String tipo);
}
