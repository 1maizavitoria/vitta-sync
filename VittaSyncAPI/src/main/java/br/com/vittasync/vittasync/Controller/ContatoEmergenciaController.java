package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.ContatoEmergenciaInputDTO;
import br.com.vittasync.vittasync.DTO.ContatoEmergenciaOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.ContatoEmergenciaService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
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
@RequestMapping("/contatoemergencia")
@Tag(name = "Contatos de emergência", description = "Cadastro, consulta, edição e exclusão dos contatos de emergência do paciente "
        + "e escolha de quais alertas cada contato recebe e por qual canal: e-mail, SMS ou ambos")
public class ContatoEmergenciaController {

    private final ContatoEmergenciaService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public ContatoEmergenciaController(ContatoEmergenciaService service,
                                       UsuarioService usuarioService,
                                       PermissaoService permissaoService) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @Operation(
            summary = "Cadastrar contato de emergência",
            description = "Cadastra um contato de emergência para o paciente, com os alertas que ele deve receber "
                    + "(sinais vitais, hábitos e estabilidade geral por nível) e os canais de envio, cada paciente pode ter "
                    + "no máximo 3 contatos. Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contato cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Limite de 3 contatos de emergência atingido"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<ContatoEmergenciaOutputDTO> cadastrar(@Parameter(description = "CPF do paciente (apenas números)")
                                                                @PathVariable String cpf,
                                                                @RequestBody ContatoEmergenciaInputDTO dto) {
        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setPaciente(paciente);
        contato.setNome(dto.getNome());
        contato.setTelefone(dto.getTelefone());
        contato.setEmail(dto.getEmail());
        contato.setReceberAlertaSinaisVitaisSaudavel(dto.getReceberAlertaSinaisVitaisSaudavel());
        contato.setReceberAlertaSinaisVitaisModerado(dto.getReceberAlertaSinaisVitaisModerado());
        contato.setReceberAlertaSinaisVitaisCritico(dto.getReceberAlertaSinaisVitaisCritico());
        contato.setReceberAlertaHabitosSaudavel(dto.getReceberAlertaHabitosSaudavel());
        contato.setReceberAlertaHabitosModerado(dto.getReceberAlertaHabitosModerado());
        contato.setReceberAlertaHabitosCritico(dto.getReceberAlertaHabitosCritico());
        contato.setReceberAlertaGeralSaudavel(dto.getReceberAlertaGeralSaudavel());
        contato.setReceberAlertaGeralModerado(dto.getReceberAlertaGeralModerado());
        contato.setReceberAlertaGeralCritico(dto.getReceberAlertaGeralCritico());
        contato.setCanalEmail(dto.getCanalEmail());
        contato.setCanalSms(dto.getCanalSms());

        ContatoEmergencia salvo = service.create(usuarioLogado.getId(), paciente, contato);
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @Operation(
            summary = "Listar contatos de emergência do paciente",
            description = "Retorna todos os contatos de emergência cadastrados para o paciente, com as preferências de alerta de cada um. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de contatos de emergência do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/listar/{cpf}")
    public ResponseEntity<List<ContatoEmergenciaOutputDTO>> listar(@Parameter(description = "CPF do paciente (apenas números)")
                                                                   @PathVariable String cpf) {
        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        List<ContatoEmergencia> contatos = service.listar(usuarioLogado.getId(), paciente);
        return ResponseEntity.ok(contatos.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    @Operation(
            summary = "Editar contato de emergência",
            description = "Substitui os dados e as preferências de alerta de um contato de emergência já cadastrado, "
                    + "a data de modificação é preenchida automaticamente pelo servidor. Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou contato não encontrados")
    })
    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<ContatoEmergenciaOutputDTO> editar(@Parameter(description = "ID do contato de emergência")
                                                             @PathVariable Integer id,
                                                             @Parameter(description = "CPF do paciente (apenas números)")
                                                             @PathVariable String cpf,
                                                             @RequestBody ContatoEmergenciaInputDTO dto) {
        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(id);
        contato.setNome(dto.getNome());
        contato.setTelefone(dto.getTelefone());
        contato.setEmail(dto.getEmail());
        contato.setPaciente(paciente);
        contato.setReceberAlertaSinaisVitaisSaudavel(dto.getReceberAlertaSinaisVitaisSaudavel());
        contato.setReceberAlertaSinaisVitaisModerado(dto.getReceberAlertaSinaisVitaisModerado());
        contato.setReceberAlertaSinaisVitaisCritico(dto.getReceberAlertaSinaisVitaisCritico());
        contato.setReceberAlertaHabitosSaudavel(dto.getReceberAlertaHabitosSaudavel());
        contato.setReceberAlertaHabitosModerado(dto.getReceberAlertaHabitosModerado());
        contato.setReceberAlertaHabitosCritico(dto.getReceberAlertaHabitosCritico());
        contato.setReceberAlertaGeralSaudavel(dto.getReceberAlertaGeralSaudavel());
        contato.setReceberAlertaGeralModerado(dto.getReceberAlertaGeralModerado());
        contato.setReceberAlertaGeralCritico(dto.getReceberAlertaGeralCritico());
        contato.setCanalEmail(dto.getCanalEmail());
        contato.setCanalSms(dto.getCanalSms());

        ContatoEmergencia atualizado = service.update(usuarioLogado.getId(), contato);
        return ResponseEntity.ok(toOutputDTO(atualizado));
    }

    @Operation(
            summary = "Excluir contato de emergência",
            description = "Exclui um contato de emergência do paciente, que deixa de receber os alertas. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contato excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou contato não encontrados")
    })
    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> deletar(@Parameter(description = "ID do contato de emergência")
                                        @PathVariable Integer id,
                                        @Parameter(description = "CPF do paciente (apenas números)")
                                        @PathVariable String cpf) {
        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        service.delete(usuarioLogado.getId(), id);
        return ResponseEntity.noContent().build();
    }

    private ContatoEmergenciaOutputDTO toOutputDTO(ContatoEmergencia entity) {
        ContatoEmergenciaOutputDTO dto = new ContatoEmergenciaOutputDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setTelefone(entity.getTelefone());
        dto.setEmail(entity.getEmail());
        dto.setDataRegistro(entity.getDataRegistro());
        dto.setDataModificacao(entity.getDataModificacao());
        dto.setReceberAlertaSinaisVitaisSaudavel(entity.getReceberAlertaSinaisVitaisSaudavel());
        dto.setReceberAlertaSinaisVitaisModerado(entity.getReceberAlertaSinaisVitaisModerado());
        dto.setReceberAlertaSinaisVitaisCritico(entity.getReceberAlertaSinaisVitaisCritico());
        dto.setReceberAlertaHabitosSaudavel(entity.getReceberAlertaHabitosSaudavel());
        dto.setReceberAlertaHabitosModerado(entity.getReceberAlertaHabitosModerado());
        dto.setReceberAlertaHabitosCritico(entity.getReceberAlertaHabitosCritico());
        dto.setReceberAlertaGeralSaudavel(entity.getReceberAlertaGeralSaudavel());
        dto.setReceberAlertaGeralModerado(entity.getReceberAlertaGeralModerado());
        dto.setReceberAlertaGeralCritico(entity.getReceberAlertaGeralCritico());
        dto.setCanalEmail(entity.getCanalEmail());
        dto.setCanalSms(entity.getCanalSms());

        return dto;
    }
}
