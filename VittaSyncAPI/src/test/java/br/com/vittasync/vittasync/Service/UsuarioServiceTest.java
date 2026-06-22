package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.UsuarioJaCadastradoException;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.SessaoToken;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.SessaoTokenRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private SessaoTokenRepository tokenRepo;
    private JwtService jwtService;
    private UsuarioService service;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        tokenRepo = mock(SessaoTokenRepository.class);
        jwtService = mock(JwtService.class);

        service = new UsuarioService(usuarioRepository, tokenRepo, jwtService);

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setCpf("12345678901");
        usuario.setEmail("teste@teste.com");
    }

    @Test
    void testCreateUsuarioNovo() {
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(false);
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = service.create(usuario);

        assertThat(resultado).isEqualTo(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCreateUsuarioCpfJaCadastrado() {
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(true);

        assertThrows(UsuarioJaCadastradoException.class,
                () -> service.create(usuario));
    }

    @Test
    void testCreateUsuarioEmailJaCadastrado() {
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(false);
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        assertThrows(UsuarioJaCadastradoException.class,
                () -> service.create(usuario));
    }

    @Test
    void testSearchByEmailEncontrado() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = service.searchByEmail("teste@teste.com");

        assertThat(resultado).isEqualTo(usuario);
    }

    @Test
    void testSearchByEmailNaoEncontrado() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.searchByEmail("teste@teste.com"));
    }

    @Test
    void testSearchByCpfEncontrado() {
        when(usuarioRepository.findByCpf("12345678901")).thenReturn(Optional.of(usuario));

        Usuario resultado = service.searchByCpf("12345678901");

        assertThat(resultado).isEqualTo(usuario);
    }

    @Test
    void testSearchByCpfNaoEncontrado() {
        when(usuarioRepository.findByCpf("12345678901")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.searchByCpf("12345678901"));
    }

    @Test
    void testSearchByIdEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario resultado = service.searchById(1);

        assertThat(resultado).isEqualTo(usuario);
    }

    @Test
    void testSearchByIdNaoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.searchById(1));
    }

    @Test
    void testUpdateUsuario() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = service.update(usuario);

        assertThat(resultado).isEqualTo(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testDeleteUsuarioDesativaTokensERemove() {
        SessaoToken token = new SessaoToken();
        token.setToken("abc123");
        token.setAtivo(true);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(tokenRepo.findAll()).thenReturn(List.of(token));
        when(jwtService.extrairCpf("abc123")).thenReturn(usuario.getCpf());

        service.delete(1);

        assertThat(token.getAtivo()).isFalse();
        verify(tokenRepo).save(token);
        verify(usuarioRepository).deleteById(1);
    }

    @Test
    void testDeleteUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.delete(1));
    }
}
