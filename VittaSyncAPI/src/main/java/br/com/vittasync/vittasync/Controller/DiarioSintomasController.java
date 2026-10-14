package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Service.PermissaoService;

import br.com.vittasync.vittasync.DTO.DiarioSintomasInputDTO;
import br.com.vittasync.vittasync.DTO.DiarioSintomasOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.DiarioSintomasService;
import br.com.vittasync.vittasync.Service.UsuarioService;
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
@RequestMapping("/diariosintomas")
@Tag(name = "Diário de sintomas", description = "Cadastro, edição, exclusão e consulta dos sintomas relatados pelo paciente "
        + "com intensidade da dor de 1 a 10")
public class DiarioSintomasController {

    private final DiarioSintomasService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public DiarioSintomasController(
            DiarioSintomasService service,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @Operation(
            summary = "Cadastrar sintoma",
            description = "Cadastra um sintoma do paciente com nome, intensidade da dor e o dia em que ocorreu, "
                    + "a data do registro é automaticamente definida pelo servidor. Após salvar, o sistema analisa alertas de dor "
                    + "intensa ou crítica. Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sintoma cadastrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<DiarioSintomasOutputDTO> create(@Parameter(description = "CPF do paciente (apenas números)")
                                                          @PathVariable String cpf,
                                                          @RequestBody DiarioSintomasInputDTO dto) {
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

        DiarioSintomas entity = new DiarioSintomas();
        entity.setPaciente(paciente);
        entity.setSintoma(dto.getSintoma());
        entity.setIntensidadeDor(dto.getIntensidadeDor());
        entity.setDataReferencia(dto.getDataReferencia());

        DiarioSintomas salvo = service.create(entity, usuarioLogado.getId());
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @Operation(
            summary = "Editar sintoma",
            description = "Substitui os dados de um sintoma já cadastrado, a data de modificação é preenchida automaticamente pelo servidor. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sintoma atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou sintoma não encontrados")
    })
    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<DiarioSintomasOutputDTO> update(@Parameter(description = "ID do sintoma")
                                                          @PathVariable Integer id,
                                                          @Parameter(description = "CPF do paciente (apenas números)")
                                                          @PathVariable String cpf,
                                                          @RequestBody DiarioSintomasInputDTO dto) {
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

        DiarioSintomas entity = new DiarioSintomas();
        entity.setPaciente(paciente);
        entity.setSintoma(dto.getSintoma());
        entity.setIntensidadeDor(dto.getIntensidadeDor());
        entity.setDataReferencia(dto.getDataReferencia());

        DiarioSintomas atualizado = service.update(id, entity, usuarioLogado.getId());
        return ResponseEntity.ok(toOutputDTO(atualizado));
    }

    @Operation(
            summary = "Excluir sintoma",
            description = "Exclui um sintoma do paciente e registra o evento na linha do tempo do paciente. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sintoma excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou sintoma não encontrados")
    })
    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do sintoma")
                                       @PathVariable Integer id,
                                       @Parameter(description = "CPF do paciente (apenas números)")
                                       @PathVariable String cpf) {
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

        service.delete(id,usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar sintomas do paciente",
            description = "Retorna todos os sintomas cadastrados do paciente. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de sintomas do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/getSintomas/{cpf}")
    public ResponseEntity<List<DiarioSintomasOutputDTO>> list(@Parameter(description = "CPF do paciente (apenas números)")
                                                              @PathVariable String cpf) {
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

        List<DiarioSintomas> lista = service.findByPacienteCpf(cpf);
        return ResponseEntity.ok(lista.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    private DiarioSintomasOutputDTO toOutputDTO(DiarioSintomas entity) {
        DiarioSintomasOutputDTO dto = new DiarioSintomasOutputDTO();
        dto.setId(entity.getId());
        dto.setSintoma(entity.getSintoma());
        dto.setIntensidadeDor(entity.getIntensidadeDor());
        dto.setDataReferencia(entity.getDataReferencia());
        dto.setDataRegistro(entity.getDataRegistro());
        dto.setDataModificacao(entity.getDataModificacao());
        return dto;
    }
}
