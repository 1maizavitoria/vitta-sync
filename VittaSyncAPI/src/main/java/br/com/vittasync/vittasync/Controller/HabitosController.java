package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.HabitosInputDTO;
import br.com.vittasync.vittasync.DTO.HabitosOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/habitos")
public class HabitosController {

    private final HabitosService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;
    private final EventoPacienteService eventoPacienteService;
    private final SinaisVitaisService sinaisVitaisService;
    private final EstabilidadeClinicaService estabilidadeClinicaService;

    public HabitosController(
            HabitosService service,
            UsuarioService usuarioService,
            PermissaoService permissaoService,
            EventoPacienteService eventoPacienteService,
            SinaisVitaisService sinaisVitaisService,
            EstabilidadeClinicaService estabilidadeClinicaService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
        this.eventoPacienteService = eventoPacienteService;
        this.sinaisVitaisService = sinaisVitaisService;
        this.estabilidadeClinicaService = estabilidadeClinicaService;
    }


    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<HabitosOutputDTO> create(@PathVariable String cpf,
                                                   @RequestBody HabitosInputDTO dto) {

        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para editar o paciente");
        }

        Habitos entity = new Habitos();
        entity.setPaciente(paciente);
        entity.setHorasSono(dto.getHorasSono());
        entity.setMinutosExercicio(dto.getMinutosExercicio());
        entity.setDataReferencia(dto.getDataReferencia());
        entity.setCanal(dto.getCanal());

        Habitos salvo = service.create(entity, usuarioLogado.getId());

        List<SinaisVitais> sinaisPaciente = sinaisVitaisService.findByPacienteCpf(cpf);
        List<Habitos> habitosPaciente = service.findByPacienteCpf(cpf);
        estabilidadeClinicaService.verificarMudancaEstabilidade(paciente.getId(), sinaisPaciente, habitosPaciente);

        return ResponseEntity.ok(toOutputDTO(salvo));
    }


    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<HabitosOutputDTO> update(@PathVariable Integer id,
                                                   @PathVariable String cpf,
                                                   @RequestBody HabitosInputDTO dto) {

        Usuario paciente = usuarioService.searchByCpf(cpf);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para editar o paciente");
        }

        Habitos entity = new Habitos();
        entity.setHorasSono(dto.getHorasSono());
        entity.setMinutosExercicio(dto.getMinutosExercicio());
        entity.setDataReferencia(dto.getDataReferencia());
        entity.setCanal(dto.getCanal());

        Habitos atualizado = service.update(id, entity, usuarioLogado.getId());

        List<SinaisVitais> sinaisPaciente = sinaisVitaisService.findByPacienteCpf(cpf);
        List<Habitos> habitosPaciente = service.findByPacienteCpf(cpf);
        estabilidadeClinicaService.verificarMudancaEstabilidade(paciente.getId(), sinaisPaciente, habitosPaciente);

        return ResponseEntity.ok(toOutputDTO(atualizado));
    }


    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
                                       @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para editar o paciente");
        }

        service.delete(id, usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getHabitos/{cpf}")
    public ResponseEntity<List<HabitosOutputDTO>> list(@PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        List<Habitos> lista = service.findByPacienteCpf(cpf);
        return ResponseEntity.ok(lista.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    private HabitosOutputDTO toOutputDTO(Habitos entity) {
        HabitosOutputDTO dto = new HabitosOutputDTO();
        dto.setId(entity.getId());
        dto.setHorasSono(entity.getHorasSono());
        dto.setMinutosExercicio(entity.getMinutosExercicio());
        dto.setIndiceRepouso(entity.getIndiceRepouso());
        dto.setRepouso(entity.getRepouso());
        dto.setCanal(entity.getCanal());
        dto.setDataReferencia(entity.getDataReferencia());
        dto.setDataRegistro(entity.getDataRegistro());
        dto.setDataModificacao(entity.getDataModificacao());
        return dto;
    }
}
