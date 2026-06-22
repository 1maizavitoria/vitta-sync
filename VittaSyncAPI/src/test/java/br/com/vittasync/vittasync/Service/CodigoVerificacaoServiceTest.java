package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.CodigoVerificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CodigoVerificacaoServiceTest {

    private CodigoVerificacaoRepository repository;
    private CodigoVerificacaoService service;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        repository = mock(CodigoVerificacaoRepository.class);
        service = new CodigoVerificacaoService(repository);

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setCpf("12345678901");
        usuario.setEmail("teste@teste.com");
    }

    @Test
    void testGerarCodigoMarcaAntigosComoUsados() {
        CodigoVerificacao antigo = new CodigoVerificacao();
        antigo.setUtilizado(false);

        when(repository.findByUsuarioIdAndTipoAndUtilizadoFalse(usuario.getId(), "LOGIN"))
                .thenReturn(List.of(antigo));

        CodigoVerificacao novo = new CodigoVerificacao();
        novo.setCodigo("12345");
        when(repository.save(any(CodigoVerificacao.class))).thenReturn(novo);

        CodigoVerificacao resultado = service.gerarCodigo(usuario, "LOGIN");

        assertThat(resultado.getCodigo()).isEqualTo("12345");
        verify(repository, times(1)).save(antigo);
        verify(repository, atLeastOnce()).save(any(CodigoVerificacao.class));
    }

    @Test
    void testValidarCodigoValidoMarcaComoUsado() {
        CodigoVerificacao cv = new CodigoVerificacao();
        cv.setCodigo("99999");
        cv.setUsuario(usuario);
        cv.setUtilizado(false);

        when(repository.findCodigoValido("99999", "LOGIN")).thenReturn(Optional.of(cv));

        CodigoVerificacao resultado = service.validarCodigo("99999", "LOGIN");

        assertThat(resultado.getCodigo()).isEqualTo("99999");
        assertThat(resultado.getUtilizado()).isTrue();
        verify(repository, times(1)).save(cv);
    }

    @Test
    void testValidarCodigoInvalidoLancaExcecao() {
        when(repository.findCodigoValido("00000", "LOGIN")).thenReturn(Optional.empty());

        assertThrows(DadosInvalidosException.class,
                () -> service.validarCodigo("00000", "LOGIN"));
    }
}
