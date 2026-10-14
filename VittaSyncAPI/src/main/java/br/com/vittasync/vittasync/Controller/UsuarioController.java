package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.UsuarioInputDTO;
import br.com.vittasync.vittasync.DTO.UsuarioOutputDTO;
import br.com.vittasync.vittasync.DTO.UsuarioUpdateDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Util.HashUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuários", description = "Cadastro, edição, consulta e exclusão de contas dos tipos paciente, responsável e profissional de saúde")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public UsuarioController(UsuarioService usuarioService, PermissaoService permissaoService) {
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cadastra uma nova conta, a data de cadastro é preenchida automaticamente pelo servidor, "
                    + "o conselho é obrigatório para o tipo ''saude''. Endpoint público que não exige token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CPF ou e-mail já cadastrado")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioOutputDTO> create(@Valid @RequestBody UsuarioInputDTO dto) {

        if ("saude".equalsIgnoreCase(dto.getTipo()) && (dto.getConselho() == null || dto.getConselho().isBlank())) {
            throw new DadosInvalidosException("Conselho é obrigatório para usuários do tipo saude");
        }

        Usuario usuario = new Usuario();
        usuario.setCpf(dto.getCpf());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setPesoInicial(dto.getPesoInicial());
        usuario.setAltura(dto.getAltura());
        usuario.setFuncaoResponsavel(dto.getFuncaoResponsavel());
        usuario.setSenha(HashUtil.hashSenha(dto.getSenha()));
        usuario.setTipo(dto.getTipo());
        usuario.setConselho(dto.getConselho());
        usuario.setDataNascimento(dto.getDataNascimento());

        Usuario criado = usuarioService.create(usuario);
        return ResponseEntity.ok(toOutputDTO(criado));
    }

    @Operation(
            summary = "Editar usuário",
            description = "Atualiza os dados não únicos de perfil do usuário, CPF, tipo. "
                    + "Permitido ao próprio usuário e aos responsáveis vinculados quando o usuário é paciente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/editar/{cpf}")
    public ResponseEntity<UsuarioOutputDTO> update(@Parameter(description = "CPF do usuário (apenas números)")
                                                   @PathVariable String cpf, @Valid @RequestBody UsuarioUpdateDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario usuarioPaciente = usuarioService.searchByCpf(cpf);
        boolean podeEditar = usuarioLogado.getCpf().equals(cpf) ||
                permissaoService.podeEditarPaciente(usuarioLogado.getId(), usuarioPaciente.getId());

        if (!podeEditar) {
            throw new AcessoNegadoException("Usuário sem permissão para editar o paciente");
        }

        Usuario usuario = usuarioService.searchByCpf(cpf);

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setAltura(dto.getAltura());
        usuario.setFuncaoResponsavel(dto.getFuncaoResponsavel());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setTelefone(dto.getTelefone());
        usuario.setPesoInicial(dto.getPesoInicial());

        Usuario atualizado = usuarioService.update(usuario);

        return ResponseEntity.ok(toOutputDTO(atualizado));
    }

    @Operation(
            summary = "Consultar usuário",
            description = "Retorna os dados de perfil do usuário. "
                    + "Permitido ao próprio usuário e a todos os usuários vinculados a ele quando é paciente (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do usuário"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/getUsuario/{cpf}")
    public ResponseEntity<UsuarioOutputDTO> getByCpf(@Parameter(description = "CPF do usuário (apenas números)")
                                                     @PathVariable String cpf) {

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        Usuario usuarioPaciente = usuarioService.searchByCpf(cpf);

        boolean podeVisualizar =
                usuarioLogado.getCpf().equals(cpf) ||
                        permissaoService.podeVisualizarPaciente(
                                usuarioLogado.getId(),
                                usuarioPaciente.getId()
                        );
        if (!podeVisualizar) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        Usuario usuario = usuarioService.searchByCpf(cpf);
        return ResponseEntity.ok(toOutputDTO(usuario));
    }

    @Operation(
            summary = "Excluir conta",
            description = "Exclui a conta do usuário logado e encerra as suas sessões. "
                    + "Permitido apenas ao próprio usuário."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta excluída com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário só pode excluir a própria conta"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/deletar/{cpf}")
    public ResponseEntity<Void> delete(@Parameter(description = "CPF do usuário (apenas números)")
                                       @PathVariable String cpf) {

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!usuarioLogado.getCpf().equals(cpf)) {
            throw new AcessoNegadoException("Usuário só pode excluir a própria conta");
        }

        Usuario usuario = usuarioService.searchByCpf(cpf);
        usuarioService.delete(usuario.getId());
        return ResponseEntity.ok().build();
    }

    private UsuarioOutputDTO toOutputDTO(Usuario usuario) {
        UsuarioOutputDTO out = new UsuarioOutputDTO();
        out.setCpf(usuario.getCpf());
        out.setNome(usuario.getNome());
        out.setEmail(usuario.getEmail());
        out.setTelefone(usuario.getTelefone());
        out.setPesoInicial(usuario.getPesoInicial());
        out.setAltura(usuario.getAltura());
        out.setFuncaoResponsavel(usuario.getFuncaoResponsavel());
        out.setTipo(usuario.getTipo());
        out.setConselho(usuario.getConselho());
        out.setDataNascimento(usuario.getDataNascimento());
        out.setDataCadastro(usuario.getDataCadastro());
        return out;
    }
}
