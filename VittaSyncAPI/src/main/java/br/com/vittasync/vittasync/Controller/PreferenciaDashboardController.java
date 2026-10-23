package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.DashboardLayoutDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.PreferenciaDashboardService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/preferencias-dashboard")
@Tag(name = "Dashboard", description = "Consulta do dashboard clínico e preferência de organização dos widgets")
public class PreferenciaDashboardController {

    private final PreferenciaDashboardService service;
    private final UsuarioService usuarioService;

    public PreferenciaDashboardController(
            PreferenciaDashboardService service,
            UsuarioService usuarioService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Consultar preferência do dashboard",
            description = "Retorna a ordem e a visibilidade dos widgets salvas pelo usuário autenticado. "
                    + "Quando ainda não existe uma preferência, retorna uma resposta sem conteúdo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preferência encontrada"),
            @ApiResponse(responseCode = "204", description = "Usuário ainda não possui preferência salva"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    @GetMapping
    public ResponseEntity<DashboardLayoutDTO> buscar() {
        Usuario usuario = usuarioService.getUsuarioLogado();

        return service.buscar(usuario)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(
            summary = "Salvar preferência do dashboard",
            description = "Cria ou substitui a ordem e a visibilidade dos widgets do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preferência salva com sucesso"),
            @ApiResponse(responseCode = "400", description = "Versão, widget ou visibilidade inválidos"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    @PutMapping
    public ResponseEntity<DashboardLayoutDTO> salvar(@RequestBody DashboardLayoutDTO dto) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        return ResponseEntity.ok(service.salvar(usuario, dto));
    }

    @Operation(
            summary = "Restaurar preferência padrão do dashboard",
            description = "Remove a preferência salva do usuário autenticado para que o frontend volte ao layout padrão."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Preferência removida ou ainda inexistente"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    @DeleteMapping
    public ResponseEntity<Void> excluir() {
        Usuario usuario = usuarioService.getUsuarioLogado();
        service.excluir(usuario);
        return ResponseEntity.noContent().build();
    }
}
