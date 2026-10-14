package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface LembreteMedicaoRepository extends JpaRepository<LembreteMedicao, Long> {

    Optional<LembreteMedicao> findByUsuarioAndAtivoTrue(Usuario usuario);

    Optional<LembreteMedicao> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);

    List<LembreteMedicao> findByAtivoTrue();

    @Query(value = "CALL sp_lembretes_do_minuto(:dia, :hora)", nativeQuery = true)
    List<LembreteMedicao> buscarLembretesDoMinuto(@Param("dia") String dia, @Param("hora") LocalTime hora);
}
