package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.PreferenciaDashboard;
import br.com.vittasync.vittasync.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface PreferenciaDashboardRepository extends JpaRepository<PreferenciaDashboard, Long> {

    Optional<PreferenciaDashboard> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}
