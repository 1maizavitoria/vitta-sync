package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LoginDTO;
import br.com.vittasync.vittasync.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {this.authService = authService;}

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) {
        authService.login(loginDTO.getCpf(), loginDTO.getSenha());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validar-codigo-login")
    public ResponseEntity<String> validarCodigoLogin(@RequestParam String codigo) {
        String token = authService.validarCodigoLogin(codigo);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/enviar-codigo-redefinir-senha")
    public ResponseEntity<Void> enviarCodigoRedefinirSenha(@RequestParam String email) {
        authService.enviarCodigoRedefinirSenha(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestParam String codigo, @RequestParam String novaSenha) {
        authService.redefinirSenha(codigo, novaSenha);
        return ResponseEntity.ok().build();
    }
}
