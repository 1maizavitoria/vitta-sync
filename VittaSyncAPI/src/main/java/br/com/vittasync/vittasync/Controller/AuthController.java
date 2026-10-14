package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LoginDTO;
import br.com.vittasync.vittasync.DTO.RedefinirSenhaDTO;
import br.com.vittasync.vittasync.DTO.SolicitarRedefinicaoDTO;
import br.com.vittasync.vittasync.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login em duas etapas com código de verificação enviado por e-mail ou SMS "
        + "e redefinição de senha, endpoints públicos que não exigem token")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Realizar login",
            description = "Confere o CPF e a senha do usuário e, se estiverem corretos, envia um código de verificação "
                    + "de 5 dígitos pelo canal escolhido (e-mail ou SMS), válido por 10 minutos. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Código de verificação enviado"),
            @ApiResponse(responseCode = "400", description = "Senha incorreta ou canal inválido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginDTO loginDTO
    ) {

        authService.login(
                loginDTO.getCpf(),
                loginDTO.getSenha(),
                loginDTO.getCanal()
        );

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Validar código de login",
            description = "Valida o código de verificação recebido no login e retorna o token JWT do usuário. "
                    + "O token deve ser enviado no cabeçalho ''Authorization'' (Bearer) dos endpoints "
                    + "(no Swagger basta colar no campo/botão ''Authorize'')."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token JWT do usuário"),
            @ApiResponse(responseCode = "400", description = "Código inválido, já utilizado ou expirado")
    })
    @PostMapping("/validar-codigo-login")
    public ResponseEntity<String> validarCodigoLogin(
            @Parameter(description = "Código de verificação de 5 dígitos recebido por e-mail ou SMS")
            @RequestParam String codigo
    ) {

        String token =
                authService.validarCodigoLogin(codigo);

        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Solicitar redefinição de senha",
            description = "Envia um código de verificação para redefinir a senha pelo canal escolhido (e-mail ou SMS), "
                    + "o código é válido por 10 minutos e deve ser usado em ''redefinir-senha''."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Código de redefinição enviado"),
            @ApiResponse(responseCode = "400", description = "Canal inválido"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário cadastrado com o e-mail informado")
    })
    @PostMapping("/enviar-codigo-redefinir-senha")
    public ResponseEntity<Void> enviarCodigoRedefinirSenha(
            @RequestBody SolicitarRedefinicaoDTO dto
    ) {

        authService.enviarCodigoRedefinirSenha(
                dto.getEmail(),
                dto.getCanal()
        );

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Redefinir senha",
            description = "Valida o código de redefinição recebido e troca a senha do usuário pela nova senha informada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Código inválido, já utilizado ou expirado")
    })
    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(
            @RequestBody RedefinirSenhaDTO dto
    ) {

        authService.redefinirSenha(
                dto.getCodigo(),
                dto.getNovaSenha()
        );

        return ResponseEntity.ok().build();
    }
}