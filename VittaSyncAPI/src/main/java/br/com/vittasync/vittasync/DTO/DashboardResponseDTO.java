package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Schema(description = "Dados do dashboard do paciente no período consultado")
public class DashboardResponseDTO {

    @Schema(description = "CPF do paciente", example = "52998224725")
    private String pacienteCpf;

    @Schema(description = "Nome do paciente", example = "Maria Oliveira")
    private String pacienteNome;

    @Schema(description = "Data inicial do período consultado", example = "2026-10-07")
    private LocalDate inicio;

    @Schema(description = "Data final do período consultado", example = "2026-10-13")
    private LocalDate fim;

    @Schema(description = "Séries de medições de cada categoria escolhida")
    private List<DashboardCategoriaDTO> categorias;

    @Schema(description = "Índices de estabilidade clínica de cada sinal vital, do sono, do exercício e o geral")
    private List<EstabilidadeClinicaDTO> estabilidadeClinica;

    @Schema(description = "Linha de base personalizada dos 7 sinais vitais, independente dos filtros")
    private List<LinhaBaseDTO> linhasBase;

    @Schema(description = "Médias diárias dos sinais vitais calculadas pelo banco, uma linha por dia com: dia, mediaPeso, "
            + "mediaFcBpm, mediaFrRpm, mediaPaSistolica, mediaPaDiastolica, mediaTempCelcius, mediaSpo2Porcento e medicoes",
            example = "[{\"dia\": \"2026-10-12\", \"mediaPeso\": 72.3, \"mediaFcBpm\": 75.0, \"mediaFrRpm\": 16.0, "
                    + "\"mediaPaSistolica\": 121.5, \"mediaPaDiastolica\": 80.0, \"mediaTempCelcius\": 36.6, "
                    + "\"mediaSpo2Porcento\": 97.0, \"medicoes\": 2}]")
    private List<Map<String, Object>> resumoDiario;

    @Schema(description = "Resumo diário dos hábitos calculado pelo banco, uma linha por dia com: dia, mediaHorasSono, "
            + "totalMinutosExercicio e registros",
            example = "[{\"dia\": \"2026-10-12\", \"mediaHorasSono\": 7.5, \"totalMinutosExercicio\": 40, \"registros\": 1}]")
    private List<Map<String, Object>> resumoHabitos;

    @Schema(description = "Quantidade de eventos do paciente no período agrupada por tipo, do mais frequente para o menos",
            example = "[{\"tipo\": \"sinais_vitais_criados\", \"quantidade\": 5}, {\"tipo\": \"febre_detectada\", \"quantidade\": 1}]")
    private List<Map<String, Object>> eventosPorTipo;

    public DashboardResponseDTO(
            String pacienteCpf,
            String pacienteNome,
            LocalDate inicio,
            LocalDate fim,
            List<DashboardCategoriaDTO> categorias,
            List<EstabilidadeClinicaDTO> estabilidadeClinica,
            List<LinhaBaseDTO> linhasBase,
            List<Map<String, Object>> resumoDiario,
            List<Map<String, Object>> resumoHabitos,
            List<Map<String, Object>> eventosPorTipo
    ) {
        this.pacienteCpf = pacienteCpf;
        this.pacienteNome = pacienteNome;
        this.inicio = inicio;
        this.fim = fim;
        this.categorias = categorias;
        this.estabilidadeClinica = estabilidadeClinica;
        this.linhasBase = linhasBase;
        this.resumoDiario = resumoDiario;
        this.resumoHabitos = resumoHabitos;
        this.eventosPorTipo = eventosPorTipo;
    }

    public String getPacienteCpf() {return pacienteCpf;}

    public String getPacienteNome() {return pacienteNome;}

    public LocalDate getInicio() {return inicio;}

    public LocalDate getFim() {return fim;}

    public List<DashboardCategoriaDTO> getCategorias() {return categorias;}

    public List<EstabilidadeClinicaDTO> getEstabilidadeClinica() {return estabilidadeClinica;}

    public List<LinhaBaseDTO> getLinhasBase() {return linhasBase;}

    public List<Map<String, Object>> getResumoDiario() {return resumoDiario;}

    public List<Map<String, Object>> getResumoHabitos() {return resumoHabitos;}

    public List<Map<String, Object>> getEventosPorTipo() {return eventosPorTipo;}
    
}
