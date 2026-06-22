package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.SessaoToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SessaoTokenRepositoryTest {

    @Autowired
    private SessaoTokenRepository sessaoTokenRepository;

    @BeforeEach
    void limparBanco() {
        sessaoTokenRepository.deleteAll();
    }

    private SessaoToken criarToken(String valor, boolean ativo) {
        SessaoToken t = new SessaoToken();
        t.setToken(valor);
        t.setAtivo(ativo);
        t.setCriadoEm(LocalDateTime.now());
        return sessaoTokenRepository.save(t);
    }

    @Test
    void testFindByToken() {
        criarToken("TOKEN123", true);

        Optional<SessaoToken> resultado = sessaoTokenRepository.findByToken("TOKEN123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getToken()).isEqualTo("TOKEN123");
        assertThat(resultado.get().getAtivo()).isTrue();
    }

    @Test
    void testFindByTokenNaoExistente() {
        Optional<SessaoToken> resultado = sessaoTokenRepository.findByToken("INEXISTENTE");

        assertThat(resultado).isNotPresent();
    }
}
