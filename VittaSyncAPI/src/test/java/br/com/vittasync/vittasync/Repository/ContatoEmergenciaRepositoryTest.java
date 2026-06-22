package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.ContatoEmergencia;
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
class ContatoEmergenciaRepositoryTest {

    @Autowired
    private ContatoEmergenciaRepository contatoEmergenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        contatoEmergenciaRepository.deleteAll();
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

    private ContatoEmergencia criarContato(Usuario paciente, String nome, String telefone) {
        ContatoEmergencia c = new ContatoEmergencia();
        c.setPaciente(paciente);
        c.setNome(nome);
        c.setTelefone(telefone);
        c.setDataRegistro(LocalDateTime.now());
        c.setDataModificacao(LocalDateTime.now());
        return contatoEmergenciaRepository.save(c);
    }

    @Test
    void testFindByPaciente() {
        Usuario paciente = criarUsuario("Paciente Teste", "PACIENTE");

        criarContato(paciente, "Maria da Silva", "41999999999");
        criarContato(paciente, "João Souza", "41988888888");

        List<ContatoEmergencia> contatos = contatoEmergenciaRepository.findByPaciente(paciente);

        assertThat(contatos).hasSize(2);
        assertThat(contatos.get(0).getPaciente().getId()).isEqualTo(paciente.getId());
    }

    @Test
    void testCountByPaciente() {
        Usuario paciente = criarUsuario("Paciente 2", "PACIENTE");

        criarContato(paciente, "Carlos Oliveira", "41977777777");
        criarContato(paciente, "Ana Costa", "41966666666");

        int quantidade = contatoEmergenciaRepository.countByPaciente(paciente);

        assertThat(quantidade).isEqualTo(2);
    }
}
