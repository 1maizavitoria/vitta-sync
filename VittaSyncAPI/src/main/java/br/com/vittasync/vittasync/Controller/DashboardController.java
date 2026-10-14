package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.DashboardResponseDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.DashboardService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;


@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Dashboard para visualização da saúde do paciente em um período: séries dos sinais vitais e hábitos, "
        + "estabilidade clínica, linha de base personalizada e resumos diários calculados pelo banco")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public DashboardController(
            DashboardService dashboardService,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {
        this.dashboardService = dashboardService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @Operation(
            summary = "Consultar dashboard do paciente",
            description = "Retorna os dados do dashboard do paciente no período informado, sem preencher as datas o período padrão são os "
                    + "últimos 7 dias. Inclui as séries de cada categoria escolhida, os índices de estabilidade clínica, a linha de "
                    + "base dos 7 sinais vitais (independente dos filtros), as médias diárias dos sinais vitais e dos hábitos e a "
                    + "quantidade de eventos por tipo. Permitido ao próprio paciente e a todos os usuários vinculados a ele."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do dashboard do paciente"),
            @ApiResponse(responseCode = "400", description = "Data inicial posterior à data final ou categoria inválida"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/pacientes/{cpf}")
    public ResponseEntity<DashboardResponseDTO> consultar(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf,
            @Parameter(description = "Data inicial do período (yyyy-MM-dd), padrão: 6 dias antes da data final")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período (yyyy-MM-dd), padrão: hoje")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @Parameter(description = "Categorias separadas por vírgula, sem informar retorna todas: pressao, frequencia_cardiaca, "
                    + "frequencia_respiratoria, temperatura, saturacao, peso, sono, exercicio")
            @RequestParam(required = false) String categorias
    ) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        return ResponseEntity.ok(
                dashboardService.consultar(paciente, inicio, fim, categorias)
        );
    }
}
