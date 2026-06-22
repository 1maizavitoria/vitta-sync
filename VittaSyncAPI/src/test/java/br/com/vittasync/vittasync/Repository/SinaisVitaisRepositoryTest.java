package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.SinaisVitais;
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
class SinaisVitaisRepositoryTest {

    @Autowired
    private SinaisVitaisRepository sinaisVitaisRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        sinaisVitaisRepository.deleteAll();
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

    private SinaisVitais criarSinaisVitais(Usuario paciente, double peso, int fcBpm, int frRpm, int paSistolica, int paDiastolica, double tempCelcius, int spo2) {
        SinaisVitais s = new SinaisVitais();
        s.setPaciente(paciente);
        s.setPeso(peso);
        s.setFcBpm(fcBpm);
        s.setFrRpm(frRpm);
        s.setPaSistolica(paSistolica);
        s.setPaDiastolica(paDiastolica);
        s.setTempCelcius(tempCelcius);
        s.setSpo2Porcento(spo2);
        s.setDataRegistro(LocalDateTime.now());
        s.setDataModificacao(LocalDateTime.now());
        s.setRegistradoPorUsuarioId(paciente.getId());
        return sinaisVitaisRepository.save(s);
    }

    @Test
    void testFindByPacienteCpf() {
        Usuario paciente = criarUsuario("Paciente Teste", "PACIENTE");

        criarSinaisVitais(paciente, 70.5, 72, 18, 120, 80, 36.7, 98);
        criarSinaisVitais(paciente, 71.0, 75, 20, 118, 78, 36.8, 97);

        List<SinaisVitais> sinais = sinaisVitaisRepository.findByPacienteCpf(paciente.getCpf());

        assertThat(sinais).hasSize(2);
        assertThat(sinais.get(0).getPaciente().getCpf()).isEqualTo(paciente.getCpf());
    }
}
