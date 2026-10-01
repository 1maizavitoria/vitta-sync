package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    Optional<Usuario> buscarPorIdComBloqueio(@Param("id") Integer id);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.cpf = :cpf")
    Optional<Usuario> findByCpf(@Param("cpf") String cpf);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.cpf = :cpf")
    boolean existsByCpf(@Param("cpf") String cpf);
}
