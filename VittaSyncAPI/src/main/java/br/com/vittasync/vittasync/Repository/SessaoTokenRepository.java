package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.SessaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface SessaoTokenRepository extends JpaRepository<SessaoToken, Long> {
    Optional<SessaoToken> findByToken(String token);
}
