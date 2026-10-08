package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.*;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;
import br.com.vittasync.vittasync.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/vinculos")
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

    @PostMapping("/gerar")
    public ResponseEntity<ConviteVinculoOutputDTO> gerarCodigo() {

        Usuario usuario = usuarioService.getUsuarioLogado();

        ConviteVinculoOutputDTO output = service.gerarCodigo(usuario.getId());

        return ResponseEntity.ok(output);
    }

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

    @PostMapping("/enviar-email")
    public ResponseEntity<Void> enviarEmail(@RequestBody EnviarConviteDTO dto) {

        service.enviarConviteEmail(dto.getEmail(), dto.getCodigo());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerVinculo(@PathVariable Long id) {

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        Vinculo vinculo = service.buscarPorId(id);

        if (!permissaoService.podeRemoverVinculo(usuarioLogado.getId(), vinculo)) {
            throw new AcessoNegadoException("Usuário sem permissão para remover o vínculo");
        }

        service.removerVinculo(id, usuarioLogado.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<VinculoOutputDTO>> listar() {

        Usuario usuario = usuarioService.getUsuarioLogado();

        return ResponseEntity.ok(service.listar(usuario.getId()));
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteResumoDTO>> listarPacientes() {

        Usuario usuario = usuarioService.getUsuarioLogado();
        return ResponseEntity.ok(service.listarPacientesDoUsuario(usuario.getId()));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<VinculoOutputDTO>> listarPorPaciente(@PathVariable Integer id) {

        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), id)) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        return ResponseEntity.ok(service.listarPorPaciente(id));
    }


}