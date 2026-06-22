package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.ConviteVinculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConviteVinculoRepositoryTest {

    @Autowired
    private ConviteVinculoRepository conviteVinculoRepository;

    @BeforeEach
    void limparBanco() {
        conviteVinculoRepository.deleteAll();
    }

    private ConviteVinculo criarConvite(Integer pacienteId, String codigo, boolean ativo) {
        ConviteVinculo convite = new ConviteVinculo();
        convite.setPacienteId(pacienteId);
        convite.setCodigo(codigo);
        convite.setAtivo(ativo);
        convite.setCriadoEm(Timestamp.from(Instant.now()));
        convite.setExpiraEm(Timestamp.from(Instant.now().plusSeconds(3600)));
        return conviteVinculoRepository.save(convite);
    }

    @Test
    void testExistsByCodigo() {
        criarConvite(1, "ABC123", true);

        boolean existe = conviteVinculoRepository.existsByCodigo("ABC123");
        boolean naoExiste = conviteVinculoRepository.existsByCodigo("XYZ789");

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    void testFindByCodigo() {
        criarConvite(2, "COD45", true);

        Optional<ConviteVinculo> resultado = conviteVinculoRepository.findByCodigo("COD45");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCodigo()).isEqualTo("COD45");
        assertThat(resultado.get().getPacienteId()).isEqualTo(2);
    }
}
