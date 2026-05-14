package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LoginDTO;
import br.com.vittasync.vittasync.DTO.RedefinirSenhaDTO;
import br.com.vittasync.vittasync.DTO.SolicitarRedefinicaoDTO;
import br.com.vittasync.vittasync.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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

    @PostMapping("/validar-codigo-login")
    public ResponseEntity<String> validarCodigoLogin(
            @RequestParam String codigo
    ) {

        String token =
                authService.validarCodigoLogin(codigo);

        return ResponseEntity.ok(token);
    }

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