package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LembreteMedicaoInputDTO;
import br.com.vittasync.vittasync.DTO.LembreteMedicaoOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.LembreteMedicaoService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/lembretes")
@Tag(name = "Lembretes de medição", description = "Lembrete semanal enviado ao paciente por e-mail ou SMS para registrar as medições de saúde, "
        + "cada paciente possui no máximo um lembrete")
public class LembreteMedicaoController {

    private final LembreteMedicaoService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public LembreteMedicaoController(
            LembreteMedicaoService service,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {

        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    private LembreteMedicaoOutputDTO toOutputDTO(
            LembreteMedicao lembrete
    ) {

        return new LembreteMedicaoOutputDTO(
                lembrete.getId(),
                lembrete.getDiasSemana(),
                lembrete.getHorario(),
                lembrete.isAtivo(),
                lembrete.getEnviarEmail(),
                lembrete.getEnviarSms()
        );
    }

    @Operation(
            summary = "Cadastrar lembrete",
            description = "Cadastra o lembrete de medição do paciente com os dias da semana, o horário e os canais de envio, "
                    + "se o paciente já tiver um lembrete ele é substituído pelo novo. O lembrete é enviado ao paciente no horário informado. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lembrete cadastrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping("/registrar/{cpf}")
    public ResponseEntity<LembreteMedicaoOutputDTO> create(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf,
            @RequestBody LembreteMedicaoInputDTO request
    ) {

        Usuario usuarioLogado =
                usuarioService.getUsuarioLogado();

        Usuario paciente =
                usuarioService.searchByCpf(cpf);

        if (
                !permissaoService.podeEditarPaciente(
                        usuarioLogado.getId(),
                        paciente.getId()
                )
        ) {

            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        LembreteMedicao lembrete =
                new LembreteMedicao();

        lembrete.setUsuario(paciente);

        lembrete.setDiasSemana(
                request.getDiasSemana()
                        .toUpperCase()
        );

        lembrete.setHorario(
                request.getHorario()
                        .withSecond(0)
                        .withNano(0)
        );

        lembrete.setAtivo(
                request.isAtivo()
        );

        lembrete.setEnviarEmail(
                request.getEnviarEmail()
        );

        lembrete.setEnviarSms(
                request.getEnviarSms()
        );

        LembreteMedicao salvo =
                service.salvarSubstituir(
                        lembrete,
                        usuarioLogado.getId()
                );

        return ResponseEntity.ok(
                toOutputDTO(salvo)
        );
    }

    @Operation(
            summary = "Consultar lembrete do paciente",
            description = "Retorna o lembrete de medição do paciente. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lembrete do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado ou sem lembrete cadastrado")
    })
    @GetMapping("/getLembrete/{cpf}")
    public ResponseEntity<LembreteMedicaoOutputDTO> getLembrete(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf
    ) {

        Usuario usuarioLogado =
                usuarioService.getUsuarioLogado();

        Usuario paciente =
                usuarioService.searchByCpf(cpf);

        if (
                !permissaoService.podeVisualizarPaciente(
                        usuarioLogado.getId(),
                        paciente.getId()
                )
        ) {

            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        return service.searchlembrete(paciente)
                .map(l ->
                        ResponseEntity.ok(
                                toOutputDTO(l)
                        )
                )
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Ativar lembrete",
            description = "Ativa o lembrete de medição do paciente, voltando a enviar nos dias e horário cadastrados. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lembrete ativado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado ou sem lembrete cadastrado")
    })
    @PutMapping("/ativar/{cpf}")
    public ResponseEntity<LembreteMedicaoOutputDTO> ativar(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf
    ) {

        Usuario usuarioLogado =
                usuarioService.getUsuarioLogado();

        Usuario paciente =
                usuarioService.searchByCpf(cpf);

        if (
                !permissaoService.podeEditarPaciente(
                        usuarioLogado.getId(),
                        paciente.getId()
                )
        ) {

            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        return service.ativar(
                        paciente,
                        usuarioLogado.getId()
                )
                .map(l ->
                        ResponseEntity.ok(
                                toOutputDTO(l)
                        )
                )
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Desativar lembrete",
            description = "Desativa o lembrete de medição do paciente sem excluir, os envios param até ser reativado. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lembrete desativado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado ou sem lembrete cadastrado")
    })
    @PutMapping("/desativar/{cpf}")
    public ResponseEntity<LembreteMedicaoOutputDTO> desativar(
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf
    ) {

        Usuario usuarioLogado =
                usuarioService.getUsuarioLogado();

        Usuario paciente =
                usuarioService.searchByCpf(cpf);

        if (
                !permissaoService.podeEditarPaciente(
                        usuarioLogado.getId(),
                        paciente.getId()
                )
        ) {

            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        return service.desativar(
                        paciente,
                        usuarioLogado.getId()
                )
                .map(l ->
                        ResponseEntity.ok(
                                toOutputDTO(l)
                        )
                )
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }
}
