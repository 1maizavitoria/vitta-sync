package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VinculoRepositoryTest {

    @Autowired
    private VinculoRepository vinculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        vinculoRepository.deleteAll();
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

    private Vinculo criarVinculo(Usuario paciente, Usuario usuario, String tipo, String funcao) {
        Vinculo v = new Vinculo();
        v.setPacienteId(paciente.getId());
        v.setUsuarioId(usuario.getId());
        v.setTipo(tipo);
        v.setFuncao(funcao);
        v.setCriadoEm(Timestamp.from(Instant.now()));
        return vinculoRepository.save(v);
    }

    @Test
    void testExistsByPacienteIdAndUsuarioId() {
        Usuario paciente = criarUsuario("Paciente Teste", "PACIENTE");
        Usuario usuario = criarUsuario("Médico Teste", "MEDICO");

        criarVinculo(paciente, usuario, "RESPONSAVEL", "Médico responsável");

        boolean existe = vinculoRepository.existsByPacienteIdAndUsuarioId(paciente.getId(), usuario.getId());
        boolean naoExiste = vinculoRepository.existsByPacienteIdAndUsuarioId(paciente.getId(), 9999);

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    void testExistsByPacienteIdAndUsuarioIdAndTipo() {
        Usuario paciente = criarUsuario("Paciente 2", "PACIENTE");
        Usuario usuario = criarUsuario("Nutricionista Teste", "MEDICO");

        criarVinculo(paciente, usuario, "NUTRICIONISTA", "Nutricionista responsável");

        boolean existe = vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(paciente.getId(), usuario.getId(), "NUTRICIONISTA");
        boolean naoExiste = vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(paciente.getId(), usuario.getId(), "FISIOTERAPEUTA");

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    void testFindByPacienteId() {
        Usuario paciente = criarUsuario("Paciente 3", "PACIENTE");
        Usuario usuario1 = criarUsuario("Usuário A", "MEDICO");
        Usuario usuario2 = criarUsuario("Usuário B", "MEDICO");

        criarVinculo(paciente, usuario1, "RESPONSAVEL", "Médico responsável");
        criarVinculo(paciente, usuario2, "NUTRICIONISTA", "Nutricionista responsável");

        List<Vinculo> vinculos = vinculoRepository.findByPacienteId(paciente.getId());

        assertThat(vinculos).hasSize(2);
        assertThat(vinculos.get(0).getPacienteId()).isEqualTo(paciente.getId());
    }

    @Test
    void testFindByUsuarioId() {
        Usuario paciente1 = criarUsuario("Paciente 4", "PACIENTE");
        Usuario paciente2 = criarUsuario("Paciente 5", "PACIENTE");
        Usuario usuario = criarUsuario("Usuário Comum", "MEDICO");

        criarVinculo(paciente1, usuario, "RESPONSAVEL", "Médico responsável");
        criarVinculo(paciente2, usuario, "NUTRICIONISTA", "Nutricionista responsável");

        List<Vinculo> vinculos = vinculoRepository.findByUsuarioId(usuario.getId());

        assertThat(vinculos).hasSize(2);
        assertThat(vinculos.get(0).getUsuarioId()).isEqualTo(usuario.getId());
    }
}
