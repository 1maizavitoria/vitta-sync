package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.Service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sessao")
@Tag(name = "Sessão", description = "Encerramento da sessão do usuário logado")
public class SessaoController {

    private final SessaoService sessaoService;

    public SessaoController(SessaoService sessaoService) {this.sessaoService = sessaoService;}

    @Operation(
            summary = "Realizar logout",
            description = "Encerra a sessão do token usado na requisição, depois disso o token deixa de ser aceito e é preciso fazer login novamente. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou já encerrado")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Parameter(hidden = true)
                                         @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        sessaoService.logout(token);
        return ResponseEntity.ok("Logout realizado com sucesso.");
    }
}
