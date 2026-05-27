package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.DTO.EventoPacienteOutputDTO;
import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.EventoPacienteService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
public class EventoPacienteController {

    private final EventoPacienteService service;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public EventoPacienteController(
            EventoPacienteService service,
            JwtService jwtService,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {
        this.service = service;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<EventoPacienteOutputDTO>> listarPorPaciente(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token =
                authHeader.replace("Bearer ", "");

        String cpfDoToken =
                jwtService.extrairCpf(token);

        Usuario usuarioLogado =
                usuarioService.searchByCpf(cpfDoToken);

        if (
                !permissaoService.podeVisualizarPaciente(
                        usuarioLogado.getId(),
                        id
                )
        ) {
            return ResponseEntity.status(403).build();
        }

        List<EventoPaciente> eventos =
                service.listarPorPaciente(id);

        List<EventoPacienteOutputDTO> output =
                eventos.stream()
                        .map(this::toOutputDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(output);
    }

    @GetMapping("/paciente/{id}/nao-visualizados")
    public ResponseEntity<Long> contarNaoVisualizados(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token =
                authHeader.replace("Bearer ", "");

        String cpfDoToken =
                jwtService.extrairCpf(token);

        Usuario usuarioLogado =
                usuarioService.searchByCpf(cpfDoToken);

        if (
                !permissaoService.podeVisualizarPaciente(
                        usuarioLogado.getId(),
                        id
                )
        ) {
            return ResponseEntity.status(403).build();
        }

        Long total = service.contarNaoVisualizados(id, usuarioLogado.getId());

        return ResponseEntity.ok(total);
    }

    @PutMapping("/paciente/{id}/visualizar")
    public ResponseEntity<Void> visualizarEventos(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);
        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);

        if (
                !permissaoService.podeVisualizarPaciente(
                        usuarioLogado.getId(),
                        id
                )
        ) {
            return ResponseEntity.status(403).build();
        }

        service.marcarComoVisualizados(
                id,
                usuarioLogado.getId()
        );

        return ResponseEntity.ok().build();
    }

    private EventoPacienteOutputDTO toOutputDTO(
            EventoPaciente entity
    ) {

        EventoPacienteOutputDTO dto = new EventoPacienteOutputDTO();
        Usuario usuario = usuarioService.searchById(entity.getUsuarioId());

        dto.setId(entity.getId());
        dto.setPacienteId(entity.getPacienteId());
        dto.setUsuarioId(entity.getUsuarioId());
        dto.setTipoEvento(entity.getTipoEvento());
        dto.setTitulo(entity.getTitulo());
        dto.setDescricao(entity.getDescricao());
        dto.setVisualizado(entity.getVisualizado());
        dto.setCriadoEm(entity.getCriadoEm());
        dto.setPrioridade(entity.getPrioridade());
        dto.setUsuarioNome(usuario.getNome());
        dto.setUsuarioTipo(usuario.getTipo());

        return dto;
    }
}