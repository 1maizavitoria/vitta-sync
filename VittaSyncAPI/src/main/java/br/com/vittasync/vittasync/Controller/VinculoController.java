package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.*;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;
import br.com.vittasync.vittasync.Service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/vinculos")
@Tag(name = "Vínculos", description = "Convites e vínculos entre o paciente e os responsáveis ou profissionais de saúde "
        + "que podem acompanhar os seus dados")
public class VinculoController {

    private final VinculoService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public VinculoController(
            VinculoService service,
            UsuarioService usuarioService,
            PermissaoService permissaoService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @Operation(
            summary = "Gerar código de convite",
            description = "Gera um código de convite de 6 caracteres e o link de entrada, válidos por 24 horas, "
                    + "para que um responsável ou profissional de saúde se vincule ao paciente. Permitido apenas ao paciente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Código de convite gerado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Somente pacientes podem gerar vínculo")
    })
    @PostMapping("/gerar")
    public ResponseEntity<ConviteVinculoOutputDTO> gerarCodigo() {

        Usuario usuario = usuarioService.getUsuarioLogado();

        ConviteVinculoOutputDTO output = service.gerarCodigo(usuario.getId());

        return ResponseEntity.ok(output);
    }

    @Operation(
            summary = "Entrar com código de convite",
            description = "Cria o vínculo do usuário logado com o paciente dono do código, informando a função conforme o tipo do usuário, "
                    + "e retorna os dados básicos do paciente. Permitido aos responsáveis e profissionais de saúde."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vínculo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Função inválida, código inativo ou expirado, ou vínculo já existente"),
            @ApiResponse(responseCode = "403", description = "Paciente não pode entrar com código"),
            @ApiResponse(responseCode = "404", description = "Código inválido")
    })
    @PostMapping("/entrar")
    public ResponseEntity<PacienteResumoDTO> entrarComCodigo(@RequestBody VinculoInputDTO dto) {

        Usuario usuario = usuarioService.getUsuarioLogado();

        PacienteResumoDTO paciente =
                service.entrarComCodigo(
                        dto.getCodigo(),
                        dto.getFuncao(),
                        usuario.getId()
                );

        return ResponseEntity.ok(paciente);
    }

    @Operation(
            summary = "Enviar convite por e-mail",
            description = "Envia o código de convite por e-mail para um responsável ou profissional de saúde já cadastrado no sistema. "
                    + "Permitido apenas ao paciente que gerou o convite."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Convite enviado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Convite inativo ou expirado, ou e-mail pertence a um paciente"),
            @ApiResponse(responseCode = "403", description = "Somente o paciente que gerou o convite pode enviá-lo"),
            @ApiResponse(responseCode = "404", description = "Convite ou usuário não encontrados")
    })
    @PostMapping("/enviar-email")
    public ResponseEntity<Void> enviarEmail(@RequestBody EnviarConviteDTO dto) {

        Usuario usuario = usuarioService.getUsuarioLogado();

        service.enviarConviteEmail(dto.getEmail(), dto.getCodigo(), usuario.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Excluir vínculo",
            description = "Exclui um vínculo e registra o evento na linha do tempo do paciente. "
                    + "Permitido ao paciente, que pode excluir qualquer vínculo dele, e ao próprio usuário vinculado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vínculo excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para remover o vínculo"),
            @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerVinculo(@Parameter(description = "ID do vínculo")
                                               @PathVariable Long id) {

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        Vinculo vinculo = service.buscarPorId(id);

        if (!permissaoService.podeRemoverVinculo(usuarioLogado.getId(), vinculo)) {
            throw new AcessoNegadoException("Usuário sem permissão para remover o vínculo");
        }

        service.removerVinculo(id, usuarioLogado.getId());

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar vínculos do usuário logado",
            description = "Para o paciente retorna os responsáveis e profissionais de saúde vinculados a ele, "
                    + "para os demais retorna os pacientes aos quais o usuário está vinculado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de vínculos do usuário logado")
    })
    @GetMapping()
    public ResponseEntity<List<VinculoOutputDTO>> listar() {

        Usuario usuario = usuarioService.getUsuarioLogado();

        return ResponseEntity.ok(service.listar(usuario.getId()));
    }

    @Operation(
            summary = "Listar pacientes acessíveis",
            description = "Retorna os pacientes que o usuário logado pode acompanhar (para o paciente retorna apenas ele mesmo)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pacientes acessíveis ao usuário logado")
    })
    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteResumoDTO>> listarPacientes() {

        Usuario usuario = usuarioService.getUsuarioLogado();
        return ResponseEntity.ok(service.listarPacientesDoUsuario(usuario.getId()));
    }

    @Operation(
            summary = "Listar vínculos do paciente",
            description = "Retorna os responsáveis e profissionais de saúde vinculados ao paciente. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de vínculos do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente")
    })
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<VinculoOutputDTO>> listarPorPaciente(@Parameter(description = "ID do paciente")
                                                                    @PathVariable Integer id) {

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), id)) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        return ResponseEntity.ok(service.listarPorPaciente(id));
    }


}