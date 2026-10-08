package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.EstabilidadeClinica;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class EstabilidadeClinicaRepositoryTest {

    @Test
    void testFindUltimaCategoriaGeral() {
        EstabilidadeClinicaRepository repository = mock(EstabilidadeClinicaRepository.class, CALLS_REAL_METHODS);

        EstabilidadeClinica ultima = new EstabilidadeClinica();
        ultima.setCategoria("moderado");

        doReturn(ultima).when(repository).findTopByPacienteIdAndTipoOrderByDataCalculoDesc(1, "geral");
        doReturn(null).when(repository).findTopByPacienteIdAndTipoOrderByDataCalculoDesc(2, "geral");

        assertThat(repository.findUltimaCategoriaGeral(1)).isEqualTo("moderado");
        assertThat(repository.findUltimaCategoriaGeral(2)).isNull();
    }
}
