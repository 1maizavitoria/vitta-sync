package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.SessaoToken;
import br.com.vittasync.vittasync.Repository.SessaoTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SessaoServiceTest {

    private SessaoTokenRepository tokenRepo;
    private JwtService jwtService;
    private SessaoService service;

    @BeforeEach
    void setup() {
        tokenRepo = mock(SessaoTokenRepository.class);
        jwtService = mock(JwtService.class);
        service = new SessaoService(tokenRepo, jwtService);
    }

    @Test
    void testRegistrarTokenSalvaAtivo() {
        String token = "abc123";

        service.registrarToken(token);

        verify(tokenRepo).save(any(SessaoToken.class));
    }

    @Test
    void testLogoutTokenValidoDesativa() {
        String token = "abc123";
        SessaoToken st = new SessaoToken();
        st.setToken(token);
        st.setAtivo(true);

        when(jwtService.validarToken(token)).thenReturn(true);
        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(st));

        service.logout(token);

        assertThat(st.getAtivo()).isFalse();
        verify(tokenRepo).save(st);
    }

    @Test
    void testLogoutTokenInvalidoLancaExcecao() {
        String token = "abc123";
        when(jwtService.validarToken(token)).thenReturn(false);

        assertThrows(AcessoNegadoException.class,
                () -> service.logout(token));
    }

    @Test
    void testLogoutTokenNaoEncontradoLancaExcecao() {
        String token = "abc123";
        when(jwtService.validarToken(token)).thenReturn(true);
        when(tokenRepo.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.logout(token));
    }

    @Test
    void testIsTokenAtivoValidoEAtivo() {
        String token = "abc123";
        SessaoToken st = new SessaoToken();
        st.setToken(token);
        st.setAtivo(true);

        when(jwtService.validarToken(token)).thenReturn(true);
        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(st));

        boolean ativo = service.isTokenAtivo(token);

        assertThat(ativo).isTrue();
    }

    @Test
    void testIsTokenAtivoInvalido() {
        String token = "abc123";
        when(jwtService.validarToken(token)).thenReturn(false);

        boolean ativo = service.isTokenAtivo(token);

        assertThat(ativo).isFalse();
    }

    @Test
    void testIsTokenAtivoNaoEncontrado() {
        String token = "abc123";
        when(jwtService.validarToken(token)).thenReturn(true);
        when(tokenRepo.findByToken(token)).thenReturn(Optional.empty());

        boolean ativo = service.isTokenAtivo(token);

        assertThat(ativo).isFalse();
    }

    @Test
    void testIsTokenAtivoEncontradoMasInativo() {
        String token = "abc123";
        SessaoToken st = new SessaoToken();
        st.setToken(token);
        st.setAtivo(false);

        when(jwtService.validarToken(token)).thenReturn(true);
        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(st));

        boolean ativo = service.isTokenAtivo(token);

        assertThat(ativo).isFalse();
    }
}
