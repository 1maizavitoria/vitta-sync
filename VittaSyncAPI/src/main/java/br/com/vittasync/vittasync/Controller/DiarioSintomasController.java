package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Service.PermissaoService;

import br.com.vittasync.vittasync.DTO.DiarioSintomasInputDTO;
import br.com.vittasync.vittasync.DTO.DiarioSintomasOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.DiarioSintomasService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/diariosintomas")
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

    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<DiarioSintomasOutputDTO> create(@PathVariable String cpf,
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

    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<DiarioSintomasOutputDTO> update(@PathVariable Integer id,
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

    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
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

    @GetMapping("/getSintomas/{cpf}")
    public ResponseEntity<List<DiarioSintomasOutputDTO>> list(@PathVariable String cpf) {
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
