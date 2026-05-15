package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.DTO.LembreteMedicaoInputDTO;
import br.com.vittasync.vittasync.DTO.LembreteMedicaoOutputDTO;
import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.LembreteMedicaoService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lembretes")
public class LembreteMedicaoController {

    private final LembreteMedicaoService service;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public LembreteMedicaoController(LembreteMedicaoService service,
                                     JwtService jwtService,
                                     UsuarioService usuarioService) {
        this.service = service;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    private LembreteMedicaoOutputDTO toOutputDTO(LembreteMedicao lembrete) {
        return new LembreteMedicaoOutputDTO(
                lembrete.getId(),
                lembrete.getDiasSemana(),
                lembrete.getHorario(),
                lembrete.isAtivo()
        );
    }

    @PostMapping("/registrar")
    public ResponseEntity<LembreteMedicaoOutputDTO> create(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LembreteMedicaoInputDTO request) {

        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);
        Usuario usuario = usuarioService.searchByCpf(cpfDoToken);

        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setDiasSemana(request.getDiasSemana().toUpperCase());
        lembrete.setHorario(request.getHorario().withSecond(0).withNano(0));
        lembrete.setAtivo(request.isAtivo());

        LembreteMedicao salvo = service.salvarSubstituir(lembrete);
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @GetMapping("/getLembrete")
    public ResponseEntity<LembreteMedicaoOutputDTO> getLembrete(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);
        Usuario usuario = usuarioService.searchByCpf(cpfDoToken);

        return service.searchlembrete(usuario)
                .map(l -> ResponseEntity.ok(toOutputDTO(l)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/ativar")
    public ResponseEntity<LembreteMedicaoOutputDTO> ativar(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);
        Usuario usuario = usuarioService.searchByCpf(cpfDoToken);

        return service.ativar(usuario)
                .map(l -> ResponseEntity.ok(toOutputDTO(l)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/desativar")
    public ResponseEntity<LembreteMedicaoOutputDTO> desativar(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);
        Usuario usuario = usuarioService.searchByCpf(cpfDoToken);

        return service.desativar(usuario)
                .map(l -> ResponseEntity.ok(toOutputDTO(l)))
                .orElse(ResponseEntity.notFound().build());
    }
}
