package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.EventoPacienteOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.EventoPacienteService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Util.EventoTipos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/eventos")
@Tag(name = "Eventos do paciente", description = "Notificações geradas automaticamente pelas ações e alertas do paciente: "
        + "cadastros, edições, exclusões, alertas clínicos, vínculos, metas e documentos")
public class EventoPacienteController {

    private final EventoPacienteService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public EventoPacienteController(
            EventoPacienteService service,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @Operation(
            summary = "Listar eventos do paciente",
            description = "Retorna todos os eventos do paciente com o tipo, a prioridade e o autor da ação, do mais recente ao mais antigo. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de eventos do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente")
    })
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<EventoPacienteOutputDTO>> listarPorPaciente(
            @Parameter(description = "ID do paciente")
            @PathVariable Integer id
    ) {

        Usuario usuarioLogado =
                usuarioService.getUsuarioLogado();

        if (
                !permissaoService.podeVisualizarPaciente(
                        usuarioLogado.getId(),
                        id
                )
        ) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        List<EventoPaciente> eventos =
                service.listarPorPaciente(id);

        List<EventoPacienteOutputDTO> output =
                eventos.stream()
                        .map(this::toOutputDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(output);
    }

    @Operation(
            summary = "Contar eventos não visualizados",
            description = "Retorna a quantidade de eventos do paciente que o usuário logado ainda não visualizou, usado no contador de notificações. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantidade de eventos não visualizados"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente")
    })
    @GetMapping("/paciente/{id}/nao-visualizados")
    public ResponseEntity<Long> contarNaoVisualizados(
            @Parameter(description = "ID do paciente")
            @PathVariable Integer id
    ) {

        Usuario usuarioLogado =
                usuarioService.getUsuarioLogado();

        if (
                !permissaoService.podeVisualizarPaciente(
                        usuarioLogado.getId(),
                        id
                )
        ) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        Long total = service.contarNaoVisualizados(id, usuarioLogado.getId());

        return ResponseEntity.ok(total);
    }

    @Operation(
            summary = "Marcar eventos como visualizados",
            description = "Marca todos os eventos do paciente como visualizados apenas para o usuário logado, os demais usuários vinculados não são afetados. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eventos marcados como visualizados"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente")
    })
    @PutMapping("/paciente/{id}/visualizar")
    public ResponseEntity<Void> visualizarEventos(
            @Parameter(description = "ID do paciente")
            @PathVariable Integer id
    ) {

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (
                !permissaoService.podeVisualizarPaciente(
                        usuarioLogado.getId(),
                        id
                )
        ) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
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
        dto.setTipoEvento(EventoTipos.toFrontendCode(entity.getTipoEvento()));
        dto.setTitulo(entity.getTitulo());
        dto.setDescricao(entity.getDescricao());
        dto.setMetadata(entity.getMetadata());
        dto.setVisualizado(entity.getVisualizado());
        dto.setCriadoEm(entity.getCriadoEm());
        dto.setPrioridade(entity.getPrioridade());
        dto.setUsuarioNome(usuario.getNome());
        dto.setUsuarioTipo(usuario.getTipo());

        return dto;
    }
}
