package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.ContatoEmergenciaInputDTO;
import br.com.vittasync.vittasync.DTO.ContatoEmergenciaOutputDTO;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.ContatoEmergenciaService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/contatoemergencia")
public class ContatoEmergenciaController {

    private final ContatoEmergenciaService service;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public ContatoEmergenciaController(ContatoEmergenciaService service,
                                       JwtService jwtService,
                                       UsuarioService usuarioService,
                                       PermissaoService permissaoService) {
        this.service = service;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<ContatoEmergenciaOutputDTO> cadastrar(@PathVariable String cpf,
                                                                @RequestHeader("Authorization") String authHeader,
                                                                @RequestBody ContatoEmergenciaInputDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            return ResponseEntity.status(403).build();
        }

        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setPaciente(paciente);
        contato.setNome(dto.getNome());
        contato.setTelefone(dto.getTelefone());

        ContatoEmergencia salvo = service.create(usuarioLogado.getId(), paciente, contato);
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @GetMapping("/listar/{cpf}")
    public ResponseEntity<List<ContatoEmergenciaOutputDTO>> listar(@PathVariable String cpf,
                                                                   @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<ContatoEmergencia> contatos = service.listar(usuarioLogado.getId(), paciente);
        return ResponseEntity.ok(contatos.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<ContatoEmergenciaOutputDTO> editar(@PathVariable Integer id,
                                                             @PathVariable String cpf,
                                                             @RequestHeader("Authorization") String authHeader,
                                                             @RequestBody ContatoEmergenciaInputDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            return ResponseEntity.status(403).build();
        }

        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(id);
        contato.setNome(dto.getNome());
        contato.setTelefone(dto.getTelefone());
        contato.setPaciente(paciente);

        ContatoEmergencia atualizado = service.update(usuarioLogado.getId(), contato);
        return ResponseEntity.ok(toOutputDTO(atualizado));
    }

    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id,
                                        @PathVariable String cpf,
                                        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.searchByCpf(cpfDoToken);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            return ResponseEntity.status(403).build();
        }

        service.delete(usuarioLogado.getId(), id);
        return ResponseEntity.noContent().build();
    }

    private ContatoEmergenciaOutputDTO toOutputDTO(ContatoEmergencia entity) {
        ContatoEmergenciaOutputDTO dto = new ContatoEmergenciaOutputDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setTelefone(entity.getTelefone());
        dto.setDataRegistro(entity.getDataRegistro());
        dto.setDataModificacao(entity.getDataModificacao());
        return dto;
    }
}
