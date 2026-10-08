package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioService usuarioService;
    private final CodigoVerificacaoService codigoService;
    private final NotificacaoService notificacaoService;
    private final JwtService jwtService;
    private final SessaoService sessaoService;

    public AuthService(
            UsuarioService usuarioService,
            CodigoVerificacaoService codigoService,
            NotificacaoService notificacaoService,
            JwtService jwtService,
            SessaoService sessaoService
    ) {

        this.usuarioService = usuarioService;
        this.codigoService = codigoService;
        this.notificacaoService = notificacaoService;
        this.jwtService = jwtService;
        this.sessaoService = sessaoService;
    }

    public void login(
            String cpf,
            String senha,
            String canal
    ) {

        Usuario usuario =
                usuarioService.searchByCpf(cpf);

        String senhaHash =
                HashUtil.hashSenha(senha);

        if (
                !usuario.getSenha()
                        .equals(senhaHash)
        ) {

            logger.warn("Tentativa de login com senha incorreta. Usuário id: {}", usuario.getId());

            throw new DadosInvalidosException(
                    "Dados inseridos inválidos"
            );
        }

        CodigoVerificacao codigo =
                codigoService.gerarCodigo(
                        usuario,
                        "LOGIN"
                );

        notificacaoService.enviarCodigo(
                usuario,
                codigo.getCodigo(),
                canal
        );

        logger.info("Código de login enviado por {}. Usuário id: {}", canal, usuario.getId());
    }

    public String validarCodigoLogin(
            String codigo
    ) {

        CodigoVerificacao cv =
                codigoService.validarCodigo(
                        codigo,
                        "LOGIN"
                );

        Usuario usuario =
                cv.getUsuario();

        String token =
                jwtService.gerarToken(
                        usuario.getCpf()
                );

        sessaoService.registrarToken(token);

        logger.info("Login realizado. Usuário id: {}", usuario.getId());

        return token;
    }

    public void enviarCodigoRedefinirSenha(
            String email,
            String canal
    ) {

        Usuario usuario =
                usuarioService.searchByEmail(email);

        CodigoVerificacao codigo =
                codigoService.gerarCodigo(
                        usuario,
                        "REDEFINIR_SENHA"
                );

        notificacaoService.enviarCodigo(
                usuario,
                codigo.getCodigo(),
                canal
        );
    }

    public void redefinirSenha(
            String codigo,
            String novaSenha
    ) {

        CodigoVerificacao cv =
                codigoService.validarCodigo(
                        codigo,
                        "REDEFINIR_SENHA"
                );

        Usuario usuario =
                cv.getUsuario();

        usuario.setSenha(
                HashUtil.hashSenha(novaSenha)
        );

        usuarioService.update(usuario);

        logger.info("Senha redefinida. Usuário id: {}", usuario.getId());
    }
}