package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.Service.*;
import br.com.vittasync.vittasync.DTO.SinaisVitaisInputDTO;
import br.com.vittasync.vittasync.DTO.SinaisVitaisOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Habitos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/sinaisvitais")
public class SinaisVitaisController {

    private final SinaisVitaisService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;
    private final EventoPacienteService eventoPacienteService;
    private final HabitosService habitosService;
    private final EstabilidadeClinicaService estabilidadeClinicaService;

    public SinaisVitaisController(
            SinaisVitaisService service,
            UsuarioService usuarioService,
            PermissaoService permissaoService,
            EventoPacienteService eventoPacienteService,
            HabitosService habitosService,
            EstabilidadeClinicaService estabilidadeClinicaService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
        this.eventoPacienteService = eventoPacienteService;
        this.habitosService = habitosService;
        this.estabilidadeClinicaService = estabilidadeClinicaService;
    }

    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<SinaisVitaisOutputDTO> create(@PathVariable String cpf,
                                                        @RequestBody SinaisVitaisInputDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para editar o paciente");
        }

        SinaisVitais entity = new SinaisVitais();
        entity.setPaciente(paciente);
        entity.setPeso(dto.getPeso());
        entity.setFcBpm(dto.getFcBpm());
        entity.setFrRpm(dto.getFrRpm());
        entity.setPaSistolica(dto.getPaSistolica());
        entity.setPaDiastolica(dto.getPaDiastolica());
        entity.setTempCelcius(dto.getTempCelcius());
        entity.setSpo2Porcento(dto.getSpo2Porcento());

        SinaisVitais salvo = service.create(entity, usuarioLogado.getId());

        List<SinaisVitais> sinaisPaciente = service.findByPacienteCpf(cpf);
        List<Habitos> habitosPaciente = habitosService.findByPacienteCpf(cpf);
        estabilidadeClinicaService.verificarMudancaEstabilidade(paciente.getId(), sinaisPaciente, habitosPaciente);

        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<SinaisVitaisOutputDTO> update(@PathVariable Integer id,
                                                        @PathVariable String cpf,
                                                        @RequestBody SinaisVitaisInputDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para editar o paciente");
        }

        SinaisVitais entity = new SinaisVitais();
        entity.setPaciente(paciente);
        entity.setPeso(dto.getPeso());
        entity.setFcBpm(dto.getFcBpm());
        entity.setFrRpm(dto.getFrRpm());
        entity.setPaSistolica(dto.getPaSistolica());
        entity.setPaDiastolica(dto.getPaDiastolica());
        entity.setTempCelcius(dto.getTempCelcius());
        entity.setSpo2Porcento(dto.getSpo2Porcento());

        SinaisVitais atualizado = service.update(id, entity, usuarioLogado.getId());
        List<SinaisVitais> sinaisPaciente = service.findByPacienteCpf(cpf);
        List<Habitos> habitosPaciente = habitosService.findByPacienteCpf(cpf);
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

    @GetMapping("/getSinaisVitais/{cpf}")
    public ResponseEntity<List<SinaisVitaisOutputDTO>> list(@PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        List<SinaisVitais> lista = service.findByPacienteCpf(cpf);

        return ResponseEntity.ok(
                lista.stream()
                        .map(this::toOutputDTO)
                        .collect(Collectors.toList())
        );
    }

    private SinaisVitaisOutputDTO toOutputDTO(SinaisVitais entity) {
        SinaisVitaisOutputDTO dto = new SinaisVitaisOutputDTO();
        dto.setId(entity.getId());
        dto.setPeso(entity.getPeso());
        dto.setFcBpm(entity.getFcBpm());
        dto.setFrRpm(entity.getFrRpm());
        dto.setPaSistolica(entity.getPaSistolica());
        dto.setPaDiastolica(entity.getPaDiastolica());
        dto.setTempCelcius(entity.getTempCelcius());
        dto.setSpo2Porcento(entity.getSpo2Porcento());
        dto.setDataRegistro(entity.getDataRegistro());
        dto.setDataModificacao(entity.getDataModificacao());
        return dto;
    }
}
