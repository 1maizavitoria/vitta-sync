package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Util.HashUtil;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final CodigoVerificacaoService codigoService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final SessaoService sessaoService;

    public AuthService(UsuarioService usuarioService,
                       CodigoVerificacaoService codigoService,
                       EmailService emailService,
                       JwtService jwtService,
                       SessaoService sessaoService) {
        this.usuarioService = usuarioService;
        this.codigoService = codigoService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.sessaoService = sessaoService;
    }

    public void login(String cpf, String senha) {
        Usuario usuario = usuarioService.searchByCpf(cpf);

        String senhaHash = HashUtil.hashSenha(senha);

        if (!usuario.getSenha().equals(senhaHash)) {
            throw new RuntimeException("Senha inválida");
        }

        CodigoVerificacao codigo = codigoService.gerarCodigo(usuario, "LOGIN");
        emailService.enviarCodigo(usuario.getEmail(), codigo.getCodigo());
    }

    public String validarCodigoLogin(String codigo) {

        CodigoVerificacao cv = codigoService.validarCodigo(codigo, "LOGIN");
        Usuario usuario = cv.getUsuario();
        String token = jwtService.gerarToken(usuario.getCpf());

        sessaoService.registrarToken(token);

        return token;
    }

    public void enviarCodigoRedefinirSenha(String email) {

        Usuario usuario = usuarioService.searchByEmail(email);
        CodigoVerificacao codigo = codigoService.gerarCodigo(usuario, "REDEFINIR_SENHA");
        emailService.enviarCodigo(usuario.getEmail(), codigo.getCodigo());
    }

    public void redefinirSenha(String codigo, String novaSenha) {

        CodigoVerificacao cv = codigoService.validarCodigo(codigo, "REDEFINIR_SENHA");
        Usuario usuario = cv.getUsuario();
        usuario.setSenha(HashUtil.hashSenha(novaSenha));
        usuarioService.update(usuario);
    }
}
