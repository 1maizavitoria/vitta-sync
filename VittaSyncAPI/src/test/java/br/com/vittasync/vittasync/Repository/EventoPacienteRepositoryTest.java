package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventoPacienteRepositoryTest {

    @Autowired
    private EventoPacienteRepository eventoPacienteRepository;

    @Autowired
    private EventoVisualizacaoRepository eventoVisualizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        eventoVisualizacaoRepository.deleteAll();
        eventoPacienteRepository.deleteAll();
        usuarioRepository.deleteAll();
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

    private EventoPaciente criarEvento(Usuario paciente, Usuario usuario, String tipo, String titulo,
                                       boolean visualizado, String prioridade, long offsetSegundos) {
        EventoPaciente e = new EventoPaciente();
        e.setPacienteId(paciente.getId()); // usa o ID do paciente
        e.setUsuarioId(usuario.getId());
        e.setTipoEvento(tipo);
        e.setTitulo(titulo);
        e.setDescricao("Descrição do evento " + titulo);
        e.setVisualizado(visualizado);
        e.setPrioridade(prioridade);
        e.setCriadoEm(Timestamp.from(Instant.now().plusSeconds(offsetSegundos)));
        return eventoPacienteRepository.save(e);
    }

    @Test
    void testFindByPacienteIdOrderByCriadoEmDesc() {
        Usuario usuario = criarUsuario("Usuário Teste", "MEDICO");
        Usuario paciente = criarUsuario("Paciente Teste", "PACIENTE");

        criarEvento(paciente, usuario, "ALERTA", "Evento Antigo", true, "BAIXA", -3600);
        criarEvento(paciente, usuario, "INFO", "Evento Recente", true, "ALTA", 0);

        List<EventoPaciente> eventos = eventoPacienteRepository.findByPacienteIdOrderByCriadoEmDesc(paciente.getId());

        assertThat(eventos).hasSize(2);
        assertThat(eventos.get(0).getTitulo()).isEqualTo("Evento Recente");
    }

    @Test
    void testCountByPacienteIdAndVisualizadoFalse() {
        Usuario usuario = criarUsuario("Usuário 2", "MEDICO");
        Usuario paciente = criarUsuario("Paciente 2", "PACIENTE");

        criarEvento(paciente, usuario, "ALERTA", "Evento 1", false, "MEDIA", 0);
        criarEvento(paciente, usuario, "INFO", "Evento 2", true, "BAIXA", 0);
        criarEvento(paciente, usuario, "INFO", "Evento 3", false, "ALTA", 0);

        Long count = eventoPacienteRepository.countByPacienteIdAndVisualizadoFalse(paciente.getId());

        assertThat(count).isEqualTo(2);
    }

    @Test
    void testFindByPacienteIdAndVisualizadoFalse() {
        Usuario usuario = criarUsuario("Usuário 3", "MEDICO");
        Usuario paciente = criarUsuario("Paciente 3", "PACIENTE");

        criarEvento(paciente, usuario, "ALERTA", "Evento 1", false, "MEDIA", 0);
        criarEvento(paciente, usuario, "INFO", "Evento 2", true, "BAIXA", 0);
        criarEvento(paciente, usuario, "INFO", "Evento 3", false, "ALTA", 0);

        List<EventoPaciente> eventosNaoVisualizados =
                eventoPacienteRepository.findByPacienteIdAndVisualizadoFalse(paciente.getId());

        assertThat(eventosNaoVisualizados).hasSize(2);
        assertThat(eventosNaoVisualizados.get(0).getVisualizado()).isFalse();
    }
}
