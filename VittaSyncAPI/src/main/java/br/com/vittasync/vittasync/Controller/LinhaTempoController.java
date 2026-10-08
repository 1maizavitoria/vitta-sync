package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LinhaTempoResponseDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.LinhaTempoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/linha-tempo")
public class LinhaTempoController {

    private final LinhaTempoService linhaTempoService;
    private final UsuarioService usuarioService;

    public LinhaTempoController(LinhaTempoService linhaTempoService,
                                UsuarioService usuarioService) {
        this.linhaTempoService = linhaTempoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<LinhaTempoResponseDTO> consultarLinhaTempo(
            @PathVariable String cpf,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim
    ) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
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
