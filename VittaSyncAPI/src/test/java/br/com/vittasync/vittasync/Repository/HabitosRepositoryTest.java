package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.Habitos;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HabitosRepositoryTest {

    @Autowired
    private HabitosRepository habitosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        habitosRepository.deleteAll();
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

    private Habitos criarHabito(Usuario paciente, int horasSono, int minutosExercicio, LocalDate dataReferencia) {
        Habitos h = new Habitos();
        h.setPaciente(paciente);
        h.setHorasSono(horasSono);
        h.setMinutosExercicio(minutosExercicio);
        h.setDataReferencia(dataReferencia);
        h.setDataRegistro(LocalDateTime.now());
        h.setDataModificacao(LocalDateTime.now());
        h.setRegistradoPorUsuarioId(paciente.getId());
        return habitosRepository.save(h);
    }

    @Test
    void testFindByPacienteCpf() {
        Usuario paciente = criarUsuario("Paciente Teste", "PACIENTE");

        criarHabito(paciente, 8, 30, LocalDate.now());
        criarHabito(paciente, 6, 45, LocalDate.now().minusDays(1));

        List<Habitos> habitos = habitosRepository.findByPacienteCpf(paciente.getCpf());

        assertThat(habitos).hasSize(2);
        assertThat(habitos.get(0).getPaciente().getCpf()).isEqualTo(paciente.getCpf());
    }
}
