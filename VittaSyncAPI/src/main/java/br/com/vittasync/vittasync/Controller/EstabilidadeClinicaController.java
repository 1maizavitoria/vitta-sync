package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.EstabilidadeClinicaDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.EstabilidadeClinicaService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/estabilidadeclinica")
public class EstabilidadeClinicaController {

    private final EstabilidadeClinicaService estabilidadeClinicaService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;
    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final HabitosRepository habitosRepository;

    public EstabilidadeClinicaController(
            EstabilidadeClinicaService estabilidadeClinicaService,
            JwtService jwtService,
            UsuarioService usuarioService,
            PermissaoService permissaoService,
            SinaisVitaisRepository sinaisVitaisRepository,
            HabitosRepository habitosRepository
    ) {
        this.estabilidadeClinicaService = estabilidadeClinicaService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.habitosRepository = habitosRepository;
    }

    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<List<EstabilidadeClinicaDTO>> consultarEstabilidade(
            @PathVariable String cpf,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            return ResponseEntity.status(403).build();
        }

        var sinais = sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(paciente.getId());
        var habitos = habitosRepository.findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
                paciente.getId(),
                LocalDate.now().minusDays(30),
                LocalDate.now()
        );

        List<EstabilidadeClinicaDTO> indices =
                estabilidadeClinicaService.calcularIndices(paciente.getId(), sinais, habitos);

        return ResponseEntity.ok(indices);
    }

    @GetMapping("/teste-alerta/{cpf}/{tipo}/{categoria}")
    public ResponseEntity<Void> testarAlerta(
            @PathVariable String cpf,
            @PathVariable String tipo,
            @PathVariable String categoria,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            return ResponseEntity.status(403).build();
        }

        EstabilidadeClinicaDTO indiceTeste = new EstabilidadeClinicaDTO(
                tipo,
                7,
                categoria,
                1.0,
                LocalDateTime.now()
        );

        EstabilidadeClinicaDTO geral = new EstabilidadeClinicaDTO(
                "geral",
                6,
                "moderado",
                1.0,
                LocalDateTime.now()
        );

        List<EstabilidadeClinicaDTO> indicesTeste = List.of(indiceTeste, geral);

        estabilidadeClinicaService.testarDisparoAlerta(paciente.getId(), indicesTeste);

        return ResponseEntity.ok().build();
    }
}
