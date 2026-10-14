package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.HabitosInputDTO;
import br.com.vittasync.vittasync.DTO.HabitosOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/habitos")
@Tag(name = "Hábitos", description = "Cadastro, edição, exclusão e consulta dos hábitos diários do paciente: horas de sono e minutos de exercício")
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


    @Operation(
            summary = "Cadastrar hábitos",
            description = "Cadastra as horas de sono e os minutos de exercício do paciente em um dia, a data do registro e o índice de repouso "
                    + "são calculados automaticamente pelo servidor. Quando o índice indica necessidade de repouso, os contatos de emergência são avisados pelo canal escolhido, "
                    + "também gera alertas de sono crítico (menos de 4 horas) e sedentarismo (menos de 10 minutos) e recalcula a estabilidade clínica. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hábitos cadastrados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<HabitosOutputDTO> create(@Parameter(description = "CPF do paciente (apenas números)")
                                                   @PathVariable String cpf,
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


    @Operation(
            summary = "Editar hábitos",
            description = "Substitui os dados de um registro de hábitos, a estabilidade clínica é recalculada junto do índice de repouso, a data de modificação "
                    + "é preenchida automaticamente pelo servidor. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hábitos atualizados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou hábitos não encontrados")
    })
    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<HabitosOutputDTO> update(@Parameter(description = "ID do registro de hábitos")
                                                   @PathVariable Integer id,
                                                   @Parameter(description = "CPF do paciente (apenas números)")
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


    @Operation(
            summary = "Excluir hábitos",
            description = "Exclui um registro de hábitos do paciente e registra o evento na linha do tempo do paciente. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Hábitos excluídos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou hábitos não encontrados")
    })
    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do registro de hábitos")
                                       @PathVariable Integer id,
                                       @Parameter(description = "CPF do paciente (apenas números)")
                                       @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para editar o paciente");
        }

        service.delete(id, usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Listar hábitos do paciente",
            description = "Retorna todos os registros de hábitos do paciente. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de hábitos do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/getHabitos/{cpf}")
    public ResponseEntity<List<HabitosOutputDTO>> list(@Parameter(description = "CPF do paciente (apenas números)")
                                                       @PathVariable String cpf) {
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
