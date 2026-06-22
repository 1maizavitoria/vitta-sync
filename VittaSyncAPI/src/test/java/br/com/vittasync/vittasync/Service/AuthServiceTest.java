package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Util.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UsuarioService usuarioService;
    private CodigoVerificacaoService codigoService;
    private NotificacaoService notificacaoService;
    private JwtService jwtService;
    private SessaoService sessaoService;
    private AuthService authService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuarioService = mock(UsuarioService.class);
        codigoService = mock(CodigoVerificacaoService.class);
        notificacaoService = mock(NotificacaoService.class);
        jwtService = mock(JwtService.class);
        sessaoService = mock(SessaoService.class);

        authService = new AuthService(usuarioService, codigoService, notificacaoService, jwtService, sessaoService);

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setCpf("12345678901");
        usuario.setEmail("teste@teste.com");
        usuario.setNome("Usuário Teste");
        usuario.setSenha(HashUtil.hashSenha("senha123"));
    }

    @Test
    void testLoginComSenhaCorretaDisparaCodigo() {
        when(usuarioService.searchByCpf("12345678901")).thenReturn(usuario);
        CodigoVerificacao codigo = new CodigoVerificacao();
        codigo.setCodigo("ABC123");
        when(codigoService.gerarCodigo(usuario, "LOGIN")).thenReturn(codigo);

        authService.login("12345678901", "senha123", "email");

        verify(notificacaoService, times(1)).enviarCodigo(usuario, "ABC123", "email");
    }

    @Test
    void testLoginComSenhaErradaLancaExcecao() {
        when(usuarioService.searchByCpf("12345678901")).thenReturn(usuario);

        assertThrows(DadosInvalidosException.class,
                () -> authService.login("12345678901", "senhaErrada", "sms"));
    }

    @Test
    void testValidarCodigoLoginRetornaToken() {
        CodigoVerificacao cv = new CodigoVerificacao();
        cv.setUsuario(usuario);
        when(codigoService.validarCodigo("ABC123", "LOGIN")).thenReturn(cv);
        when(jwtService.gerarToken(usuario.getCpf())).thenReturn("tokenXYZ");

        String token = authService.validarCodigoLogin("ABC123");

        assertThat(token).isEqualTo("tokenXYZ");
        verify(sessaoService, times(1)).registrarToken("tokenXYZ");
    }

    @Test
    void testEnviarCodigoRedefinirSenha() {
        when(usuarioService.searchByEmail("teste@teste.com")).thenReturn(usuario);
        CodigoVerificacao codigo = new CodigoVerificacao();
        codigo.setCodigo("RESET123");
        when(codigoService.gerarCodigo(usuario, "REDEFINIR_SENHA")).thenReturn(codigo);

        authService.enviarCodigoRedefinirSenha("teste@teste.com", "sms");

        verify(notificacaoService, times(1)).enviarCodigo(usuario, "RESET123", "sms");
    }

    @Test
    void testRedefinirSenhaAtualizaUsuario() {
        CodigoVerificacao cv = new CodigoVerificacao();
        cv.setUsuario(usuario);
        when(codigoService.validarCodigo("RESET123", "REDEFINIR_SENHA")).thenReturn(cv);

        authService.redefinirSenha("RESET123", "novaSenha");

        verify(usuarioService, times(1)).update(usuario);
        assertThat(usuario.getSenha()).isEqualTo(HashUtil.hashSenha("novaSenha"));
    }
}
