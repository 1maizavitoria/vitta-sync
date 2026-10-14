package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LinhaTempoResponseDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.LinhaTempoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/linha-tempo")
@Tag(name = "Linha do tempo", description = "Histórico do paciente em um período juntando sinais vitais, hábitos e sintomas em ordem cronológica")
public class LinhaTempoController {

    private final LinhaTempoService linhaTempoService;
    private final UsuarioService usuarioService;

    public LinhaTempoController(LinhaTempoService linhaTempoService,
                                UsuarioService usuarioService) {
        this.linhaTempoService = linhaTempoService;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Consultar linha do tempo do paciente",
            description = "Retorna os sinais vitais, hábitos e sintomas do paciente no período separados por categoria e também juntos em ''itens'', "
                    + "do mais recente ao mais antigo, sem datas informadas considera os últimos 7 dias. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Linha do tempo do paciente no período"),
            @ApiResponse(responseCode = "400", description = "Data inicial posterior à data final"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<LinhaTempoResponseDTO> consultarLinhaTempo(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf,
            @Parameter(description = "Data inicial do período (yyyy-MM-dd), padrão 6 dias antes da data final")
            @RequestParam(required = false) LocalDate inicio,
            @Parameter(description = "Data final do período (yyyy-MM-dd), padrão hoje")
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
