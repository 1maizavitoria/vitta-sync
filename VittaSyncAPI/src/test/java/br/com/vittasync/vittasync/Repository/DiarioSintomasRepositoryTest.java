package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.DiarioSintomas;
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
class DiarioSintomasRepositoryTest {

    @Autowired
    private DiarioSintomasRepository diarioSintomasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        diarioSintomasRepository.deleteAll();
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

    private DiarioSintomas criarDiario(Usuario paciente, String sintoma, int intensidade, LocalDate dataReferencia) {
        DiarioSintomas d = new DiarioSintomas();
        d.setPaciente(paciente);
        d.setSintoma(sintoma);
        d.setIntensidadeDor(intensidade);
        d.setDataReferencia(dataReferencia);
        d.setDataRegistro(LocalDateTime.now());
        d.setDataModificacao(LocalDateTime.now());
        d.setRegistradoPorUsuarioId(paciente.getId());
        return diarioSintomasRepository.save(d);
    }

    @Test
    void testFindByPacienteCpf() {
        Usuario paciente = criarUsuario("Paciente Teste", "PACIENTE");

        criarDiario(paciente, "Dor de cabeça", 7, LocalDate.now());
        criarDiario(paciente, "Náusea", 5, LocalDate.now().minusDays(1));

        List<DiarioSintomas> diarios = diarioSintomasRepository.findByPacienteCpf(paciente.getCpf());

        assertThat(diarios).hasSize(2);
        assertThat(diarios.get(0).getPaciente().getCpf()).isEqualTo(paciente.getCpf());
    }
}
