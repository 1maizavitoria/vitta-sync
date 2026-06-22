package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LembreteMedicaoRepositoryTest {

    @Autowired
    private LembreteMedicaoRepository lembreteMedicaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        lembreteMedicaoRepository.deleteAll();
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

    private LembreteMedicao criarLembrete(Usuario usuario, boolean ativo, String diasSemana, LocalTime horario) {
        LembreteMedicao l = new LembreteMedicao();
        l.setUsuario(usuario);
        l.setAtivo(ativo);
        l.setDiasSemana(diasSemana);
        l.setHorario(horario);
        l.setEnviarEmail(true);
        l.setEnviarSms(false);
        return lembreteMedicaoRepository.save(l);
    }

    @Test
    void testFindByUsuarioAndAtivoTrue() {
        Usuario usuario = criarUsuario("Paciente Teste", "PACIENTE");

        criarLembrete(usuario, true, "segunda,terca", LocalTime.of(8, 0));
        criarLembrete(usuario, false, "quarta", LocalTime.of(10, 0));

        Optional<LembreteMedicao> resultado = lembreteMedicaoRepository.findByUsuarioAndAtivoTrue(usuario);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().isAtivo()).isTrue();
    }

    @Test
    void testFindByUsuario() {
        Usuario usuario = criarUsuario("Paciente 2", "PACIENTE");

        criarLembrete(usuario, true, "sexta", LocalTime.of(9, 30));

        Optional<LembreteMedicao> resultado = lembreteMedicaoRepository.findByUsuario(usuario);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getUsuario().getId()).isEqualTo(usuario.getId());
    }

    @Test
    void testDeleteByUsuario() {
        Usuario usuario = criarUsuario("Paciente 3", "PACIENTE");

        criarLembrete(usuario, true, "domingo", LocalTime.of(7, 0));

        lembreteMedicaoRepository.deleteByUsuario(usuario);

        Optional<LembreteMedicao> resultado = lembreteMedicaoRepository.findByUsuario(usuario);

        assertThat(resultado).isNotPresent();
    }

    @Test
    void testFindByAtivoTrue() {
        Usuario usuario1 = criarUsuario("Paciente 4", "PACIENTE");
        Usuario usuario2 = criarUsuario("Paciente 5", "PACIENTE");

        criarLembrete(usuario1, true, "segunda", LocalTime.of(8, 0));
        criarLembrete(usuario2, false, "terça", LocalTime.of(9, 0));

        List<LembreteMedicao> ativos = lembreteMedicaoRepository.findByAtivoTrue();

        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).isAtivo()).isTrue();
    }
}
