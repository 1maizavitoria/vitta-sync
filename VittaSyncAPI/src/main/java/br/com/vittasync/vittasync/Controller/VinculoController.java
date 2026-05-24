package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.*;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Repository.VinculoRepository;

import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;

import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.VinculoService;
import br.com.vittasync.vittasync.Service.PermissaoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vinculos")
public class VinculoController {

    private final VinculoService service;

    private final JwtService jwtService;

    private final UsuarioService usuarioService;

    private final PermissaoService permissaoService;

    private final VinculoRepository vinculoRepository;

    public VinculoController(VinculoService service, JwtService jwtService, UsuarioService usuarioService, PermissaoService permissaoService, VinculoRepository vinculoRepository) {
        this.service = service;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
        this.vinculoRepository = vinculoRepository;
    }

    @PostMapping("/gerar")
    public ResponseEntity<ConviteVinculoOutputDTO> gerarCodigo(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");

        String cpf = jwtService.extrairCpf(token);

        Usuario usuario = usuarioService.searchByCpf(cpf);

        ConviteVinculoOutputDTO output = service.gerarCodigo(usuario.getId());

        return ResponseEntity.ok(output);
    }

    @PostMapping("/entrar")
    public ResponseEntity<Void> entrarComCodigo(@RequestHeader("Authorization") String authHeader, @RequestBody VinculoInputDTO dto) {

        String token = authHeader.replace("Bearer ", "");

        String cpf = jwtService.extrairCpf(token);

        Usuario usuario = usuarioService.searchByCpf(cpf);

        service.entrarComCodigo(
                dto.getCodigo(),
                dto.getFuncao(),
                usuario.getId()
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/enviar-email")
    public ResponseEntity<Void> enviarEmail(@RequestBody EnviarConviteDTO dto) {

        service.enviarConviteEmail(dto.getEmail(), dto.getCodigo());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerVinculo(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");

        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);

        Vinculo vinculo = vinculoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado"));

        if (!permissaoService.podeRemoverVinculo(usuarioLogado.getId(), vinculo)) {
            return ResponseEntity.status(403).build();
        }

        service.removerVinculo(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<VinculoOutputDTO>> listar(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");

        String cpf = jwtService.extrairCpf(token);

        Usuario usuario = usuarioService.searchByCpf(cpf);

        return ResponseEntity.ok(service.listar(usuario.getId()));
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteResumoDTO>> listarPacientes(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String cpf = jwtService.extrairCpf(token);
        Usuario usuario = usuarioService.searchByCpf(cpf);
        return ResponseEntity.ok(service.listarPacientesDoUsuario(usuario.getId()));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<VinculoOutputDTO>> listarPorPaciente(@PathVariable Integer id) {

        return ResponseEntity.ok(service.listarPorPaciente(id));
    }


}