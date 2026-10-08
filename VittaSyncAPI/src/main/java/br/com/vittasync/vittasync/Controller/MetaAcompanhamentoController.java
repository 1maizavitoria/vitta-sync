package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.MetaAcompanhamentoInputDTO;
import br.com.vittasync.vittasync.DTO.MetaAcompanhamentoOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.MetaAcompanhamento;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/metaacompanhamento")
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


    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> cadastrar(@PathVariable String cpf,
                                                                 @RequestBody MetaAcompanhamentoInputDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeCriarEditarMeta(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        MetaAcompanhamento salvo = service.create(dto, paciente, usuarioLogado.getId());
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> editar(@PathVariable Long id,
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

    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> deletar(@PathVariable Long id,
                                        @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        service.delete(id, paciente.getId(), usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/concluir/{id}/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> concluir(@PathVariable Long id,
                                                                @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeCriarEditarMeta(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        MetaAcompanhamento concluida = service.concluirMeta(id, paciente.getId(), usuarioLogado.getId());
        return ResponseEntity.ok(toOutputDTO(concluida));
    }

    @GetMapping("/getMetas/{cpf}")
    public ResponseEntity<List<MetaAcompanhamentoOutputDTO>> listar(@PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o paciente");
        }

        List<MetaAcompanhamento> lista = service.listarPorPaciente(paciente.getId(), usuarioLogado.getId());
        return ResponseEntity.ok(lista.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    @PutMapping("/atualizar-valor/{id}/{cpf}")
    public ResponseEntity<MetaAcompanhamentoOutputDTO> atualizarValorManual(
            @PathVariable Long id,
            @PathVariable String cpf,
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
