package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LinhaTempoResponseDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.LinhaTempoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/linha-tempo")
public class LinhaTempoController {

    private final LinhaTempoService linhaTempoService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public LinhaTempoController(LinhaTempoService linhaTempoService,
                                JwtService jwtService,
                                UsuarioService usuarioService) {
        this.linhaTempoService = linhaTempoService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<LinhaTempoResponseDTO> consultarLinhaTempo(
            @PathVariable String cpf,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim
    ) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);
        Usuario paciente = usuarioService.searchByCpf(cpf);

        return ResponseEntity.ok(
                linhaTempoService.consultarLinhaTempo(
                        usuarioLogado.getId(),
                        paciente.getId(),
                        inicio,
                        fim
                )
        );
    }
}
