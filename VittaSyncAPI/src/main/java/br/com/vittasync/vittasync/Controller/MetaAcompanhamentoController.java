package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.MetaAcompanhamentoInputDTO;
import br.com.vittasync.vittasync.DTO.MetaAcompanhamentoOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.MetaAcompanhamento;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/metaacompanhamento")
@Tag(name = "Metas de acompanhamento", description = "Metas de saúde do paciente com prazo e progresso calculado: peso, horas de sono, "
        + "minutos de exercício ou metas personalizadas")
public class MetaAcompanhamentoController {

    private final MetaAcompanhamentoService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public MetaAcompanhamentoController(
            MetaAcompanhamentoService service,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }


    @Operation(
            summary = "Cadastrar meta",
            description = "Cadastra uma meta para o paciente com indicador, direção, valor alvo e prazo, a meta começa ''em_andamento'' "
                    + "e o valor atual de peso, sono e exercício é buscado automaticamente do último registro do paciente, "
                    + "metas personalizadas começam com valor 0. "
                    + "Permitido ao próprio paciente, aos responsáveis vinculados e aos profissionais de saúde vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meta cadastrada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> cadastrar(@Parameter(description = "CPF do paciente (apenas números)")
                                                                 @PathVariable String cpf,
                                                                 @RequestBody MetaAcompanhamentoInputDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeCriarEditarMeta(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        MetaAcompanhamento salvo = service.create(dto, paciente, usuarioLogado.getId());
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @Operation(
            summary = "Editar meta",
            description = "Substitui os dados de uma meta do paciente e recalcula o progresso, a data de modificação é preenchida automaticamente pelo servidor. "
                    + "Permitido ao próprio paciente, aos responsáveis vinculados e aos profissionais de saúde vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meta atualizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou meta não encontrados")
    })
    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> editar(@Parameter(description = "ID da meta")
                                                              @PathVariable Long id,
                                                              @Parameter(description = "CPF do paciente (apenas números)")
                                                              @PathVariable String cpf,
                                                              @RequestBody MetaAcompanhamentoInputDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeCriarEditarMeta(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        MetaAcompanhamento atualizado = service.update(id, dto, paciente, usuarioLogado.getId());
        return ResponseEntity.ok(toOutputDTO(atualizado));
    }

    @Operation(
            summary = "Excluir meta",
            description = "Exclui uma meta do paciente e registra o evento na linha do tempo do paciente. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Meta excluída com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou meta não encontrados")
    })
    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> deletar(@Parameter(description = "ID da meta")
                                        @PathVariable Long id,
                                        @Parameter(description = "CPF do paciente (apenas números)")
                                        @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        service.delete(id, paciente.getId(), usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Concluir meta",
            description = "Conclui manualmente uma meta do paciente, o status fica ''concluido'' ou ''concluido_atrasado'' quando o prazo já passou. "
                    + "Permitido ao próprio paciente, aos responsáveis vinculados e aos profissionais de saúde vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meta concluída com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou meta não encontrados")
    })
    @PostMapping("/concluir/{id}/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> concluir(@Parameter(description = "ID da meta")
                                                                @PathVariable Long id,
                                                                @Parameter(description = "CPF do paciente (apenas números)")
                                                                @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeCriarEditarMeta(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        MetaAcompanhamento concluida = service.concluirMeta(id, paciente.getId(), usuarioLogado.getId());
        return ResponseEntity.ok(toOutputDTO(concluida));
    }

    @Operation(
            summary = "Listar metas do paciente",
            description = "Retorna todas as metas do paciente com o valor atual e o progresso atualizados, metas que atingiram o alvo "
                    + "ou passaram do prazo têm o status atualizado automaticamente. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de metas do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/getMetas/{cpf}")
    public ResponseEntity<List<MetaAcompanhamentoOutputDTO>> listar(@Parameter(description = "CPF do paciente (apenas números)")
                                                                    @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        List<MetaAcompanhamento> lista = service.listarPorPaciente(paciente.getId(), usuarioLogado.getId());
        return ResponseEntity.ok(lista.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    @Operation(
            summary = "Atualizar valor da meta personalizada",
            description = "Atualiza manualmente o valor atual de uma meta personalizada e recalcula o progresso, "
                    + "metas de peso, sono e exercício são atualizadas automaticamente pelos registros do paciente. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor da meta atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Meta não é personalizada ou valor atual não informado"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou meta não encontrados")
    })
    @PutMapping("/atualizar-valor/{id}/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> atualizarValorManual(
            @Parameter(description = "ID da meta")
            @PathVariable Long id,
            @Parameter(description = "CPF do paciente (apenas números)")
            @PathVariable String cpf,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo valor atual da meta",
                    content = @Content(examples = @ExampleObject(value = "{\"valorAtual\": 5}"))
            )
            @RequestBody Map<String, Double> body) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);
        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }
        MetaAcompanhamento meta = service.atualizarValorManual(
                id,
                paciente.getId(),
                body.get("valorAtual"),
                usuarioLogado.getId()
        );
        return ResponseEntity.ok(toOutputDTO(meta));
    }

    private MetaAcompanhamentoOutputDTO toOutputDTO(MetaAcompanhamento entity) {
        MetaAcompanhamentoOutputDTO dto = new MetaAcompanhamentoOutputDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setTipoDado(entity.getTipoDado());
        dto.setIndicador(entity.getIndicador());
        dto.setDirecao(entity.getDirecao());
        dto.setValorInicial(entity.getValorInicial());
        dto.setValorAtual(entity.getValorAtual());
        dto.setUnidade(entity.getUnidade());
        dto.setValorAlvo(entity.getValorAlvo());
        dto.setProgresso(entity.getProgresso());
        dto.setStatus(entity.getStatus());
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setDataLimite(entity.getDataLimite());
        dto.setDataConclusao(entity.getDataConclusao());
        dto.setDataModificacao(entity.getDataModificacao());
        return dto;
    }
}
