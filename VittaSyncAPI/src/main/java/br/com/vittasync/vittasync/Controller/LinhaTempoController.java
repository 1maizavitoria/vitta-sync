package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LinhaTempoSinaisVitaisDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoHabitosDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoSintomasDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoResponseDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.LinhaTempoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/linha-tempo")
public class LinhaTempoController {

    private final LinhaTempoService linhaTempoService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public LinhaTempoController(LinhaTempoService linhaTempoService,
                                JwtService jwtService,
                                UsuarioService usuarioService,
                                PermissaoService permissaoService) {
        this.linhaTempoService = linhaTempoService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
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

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<LinhaTempoSinaisVitaisDTO> sinais = linhaTempoService.consultarSinaisVitais(
                usuarioLogado.getId(), paciente.getId(), inicio, fim);

        List<LinhaTempoHabitosDTO> habitos = linhaTempoService.consultarHabitos(
                usuarioLogado.getId(), paciente.getId(), inicio, fim);

        List<LinhaTempoSintomasDTO> sintomas = linhaTempoService.consultarSintomas(
                usuarioLogado.getId(), paciente.getId(), inicio, fim);

        LinhaTempoResponseDTO response = new LinhaTempoResponseDTO(sinais, habitos, sintomas);
        return ResponseEntity.ok(response);
    }
}
