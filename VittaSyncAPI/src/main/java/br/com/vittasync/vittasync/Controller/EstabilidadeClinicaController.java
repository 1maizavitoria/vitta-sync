package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.EstabilidadeClinicaDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.EstabilidadeClinicaService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/estabilidadeclinica")
@Tag(name = "Estabilidade clínica", description = "Índices de estabilidade clínica do paciente calculados a partir dos sinais vitais "
        + "e dos hábitos, classificados em saudável, moderado ou crítico")
public class EstabilidadeClinicaController {

    private final EstabilidadeClinicaService estabilidadeClinicaService;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public EstabilidadeClinicaController(
            EstabilidadeClinicaService estabilidadeClinicaService,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {
        this.estabilidadeClinicaService = estabilidadeClinicaService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @Operation(
            summary = "Consultar estabilidade clínica do paciente",
            description = "Calcula e retorna os índices de estabilidade de cada sinal vital, do sono, do exercício e o índice geral, "
                    + "usando todos os sinais vitais e os hábitos dos últimos 30 dias (cada fator precisa de pelo menos 3 registros). "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Índices de estabilidade clínica do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<List<EstabilidadeClinicaDTO>> consultarEstabilidade(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf
    ) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        List<EstabilidadeClinicaDTO> indices =
                estabilidadeClinicaService.consultarIndices(paciente.getId());

        return ResponseEntity.ok(indices);
    }

    @Operation(
            summary = "Testar alerta de estabilidade",
            description = "Simula uma mudança de estabilidade para o fator e a categoria informados, junto de um índice geral moderado, "
                    + "e dispara os alertas para os contatos de emergência que escolheram receber esse tipo de alerta. "
                    + "Não grava nada no histórico, apenas confere o envio de e-mail e SMS. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alertas de teste disparados"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/teste-alerta/{cpf}/{tipo}/{categoria}")
    public ResponseEntity<Void> testarAlerta(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf,
            @Parameter(description = "Fator simulado: fc_bpm, fr_rpm, pressao, temp_celcius, spo2, peso, sono ou exercicio")
            @PathVariable String tipo,
            @Parameter(description = "Categoria simulada: saudavel, moderado ou critico")
            @PathVariable String categoria
    ) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
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
