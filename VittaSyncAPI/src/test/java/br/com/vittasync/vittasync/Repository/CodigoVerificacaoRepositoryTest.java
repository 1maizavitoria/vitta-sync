package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CodigoVerificacaoRepositoryTest {

    @Autowired
    private CodigoVerificacaoRepository codigoVerificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        codigoVerificacaoRepository.deleteAll();
    }

    private Usuario criarUsuario(String nome, String tipo) {
        String unique = UUID.randomUUID().toString().substring(0, 11);
        Usuario u = new Usuario();
        u.setCpf(unique);
        u.setNome(nome);
        u.setEmail("user" + unique + "@test.com");
        u.setTelefone("41" + unique);
        u.setSenha("senha" + unique);
        u.setTipo(tipo);
        u.setDataNascimento(LocalDate.of(1990, 1, 1));
        u.setDataCadastro(LocalDateTime.now());
        return usuarioRepository.save(u);
    }

    private CodigoVerificacao criarCodigo(Usuario usuario, String codigo, String tipo, boolean utilizado, LocalDateTime expira) {

        String codigoFinal = codigo.length() > 5 ? codigo.substring(0, 5) : codigo;

        CodigoVerificacao c = new CodigoVerificacao();
        c.setUsuario(usuario);
        c.setCodigo(codigoFinal);
        c.setTipo(tipo);
        c.setUtilizado(utilizado);
        c.setExpira(expira);
        return codigoVerificacaoRepository.save(c);
    }

    @Test
    void testFindCodigoValido() {
        Usuario usuario = criarUsuario("Paciente Teste", "PACIENTE");

        criarCodigo(usuario, "A1234", "EMAIL", false, LocalDateTime.now().plusMinutes(10));

        criarCodigo(usuario, "B1234", "EMAIL", false, LocalDateTime.now().minusMinutes(10));

        Optional<CodigoVerificacao> resultado = codigoVerificacaoRepository.findCodigoValido("A1234", "EMAIL");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCodigo()).isEqualTo("A1234");
    }

    @Test
    void testFindByUsuarioIdAndTipoAndUtilizadoFalse() {
        Usuario usuario = criarUsuario("Paciente 2", "PACIENTE");

        criarCodigo(usuario, "C1234", "SMS", false, LocalDateTime.now().plusMinutes(5));
        criarCodigo(usuario, "D1234", "SMS", true, LocalDateTime.now().plusMinutes(5));
        criarCodigo(usuario, "E1234", "EMAIL", false, LocalDateTime.now().plusMinutes(5));

        List<CodigoVerificacao> resultados = codigoVerificacaoRepository.findByUsuarioIdAndTipoAndUtilizadoFalse(usuario.getId(), "SMS");

        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getCodigo()).isEqualTo("C1234");
    }
}
