package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.ContatoEmergenciaInputDTO;
import br.com.vittasync.vittasync.DTO.ContatoEmergenciaOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.ContatoEmergenciaService;
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
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public ContatoEmergenciaController(ContatoEmergenciaService service,
                                       UsuarioService usuarioService,
                                       PermissaoService permissaoService) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<ContatoEmergenciaOutputDTO> cadastrar(@PathVariable String cpf,
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

    @GetMapping("/listar/{cpf}")
    public ResponseEntity<List<ContatoEmergenciaOutputDTO>> listar(@PathVariable String cpf) {
        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        List<ContatoEmergencia> contatos = service.listar(usuarioLogado.getId(), paciente);
        return ResponseEntity.ok(contatos.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<ContatoEmergenciaOutputDTO> editar(@PathVariable Integer id,
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

    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id,
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
