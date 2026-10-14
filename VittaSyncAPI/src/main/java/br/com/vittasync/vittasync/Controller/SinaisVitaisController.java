package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.Service.*;
import br.com.vittasync.vittasync.DTO.SinaisVitaisInputDTO;
import br.com.vittasync.vittasync.DTO.SinaisVitaisOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Habitos;
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
@RequestMapping("/sinaisvitais")
@Tag(name = "Sinais vitais", description = "Registro, edição, exclusão e consulta das medições de sinais vitais do paciente: "
        + "peso, frequência cardíaca e respiratória, pressão arterial, temperatura e saturação de oxigênio")
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

    @Operation(
            summary = "Cadastrar sinais vitais",
            description = "Cadastra uma nova medição de sinais vitais para o paciente, a data do registro é "
                    + "automaticamente definida pelo servidor. Após salvar, o sistema analisa alertas clínicos (febre, SpO2 baixa, etc.), "
                    + "atualiza a linha de base personalizada e recalcula a estabilidade clínica. "
                    + "Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medição cadastrada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<SinaisVitaisOutputDTO> create(@Parameter(description = "CPF do paciente (apenas números)")
                                                        @PathVariable String cpf,
                                                        @RequestBody SinaisVitaisInputDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para alterar o paciente");
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

    @Operation(
            summary = "Editar sinais vitais",
            description = "Substitui os valores de uma medição já cadastrada, data de modificação é preenchida pelo "
                    + "servidor e a estabilidade clínica é recalculada, se a linha de base de um sinal vital estiver ''em "
                    + "formação'', ela considera os novos valores. Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medição atualizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou medição não encontrados")
    })
    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<SinaisVitaisOutputDTO> update(@Parameter(description = "ID da medição")
                                                        @PathVariable Integer id,
                                                        @Parameter(description = "CPF do paciente (apenas números)")
                                                        @PathVariable String cpf,
                                                        @RequestBody SinaisVitaisInputDTO dto) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para alterar o paciente");
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

    @Operation(
            summary = "Excluir sinais vitais",
            description = "Exclui uma medição de sinais vitais e registra o evento na linha do tempo do paciente, "
                    + "linhas de base já formadas não são alteradas. Permitido ao próprio paciente e aos responsáveis vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medição excluída com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para alterar os dados do paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente ou medição não encontrados")
    })
    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> delete(@Parameter(description = "Id da medição")
                                       @PathVariable Integer id,
                                       @Parameter(description = "CPF do paciente (apenas números)")
                                       @PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeEditarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para alterar o paciente");
        }

        service.delete(id, usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar sinais vitais do paciente",
            description = "Retorna todas as medições de sinais vitais cadastradas para o paciente. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de medições do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/getSinaisVitais/{cpf}")
    public ResponseEntity<List<SinaisVitaisOutputDTO>> list(@Parameter(description = "CPF do paciente (apenas números)")
                                                            @PathVariable String cpf) {
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
