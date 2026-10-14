package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Dados do relatório para pré-visualização")
public class RelatorioPreviewDTO {
    @Schema(description = "Data inicial do período", example = "2026-10-01")
    private LocalDate dataInicio;

    @Schema(description = "Data final do período", example = "2026-10-13")
    private LocalDate dataFim;

    @Schema(description = "Data e hora em que o relatório foi gerado", example = "2026-10-13T21:30:00")
    private LocalDateTime dataEmissao;

    @Schema(description = "Categorias incluídas no relatório", example = "[\"SINAIS\", \"HABITOS\"]")
    private List<String> categorias;

    @Schema(description = "Resumo estatístico dos sinais vitais e hábitos do período")
    private RelatorioResumoDTO resumo;

    @Schema(description = "Dados básicos do paciente")
    private RelatorioPacienteResumoDTO paciente;

    @Schema(description = "Sinais vitais do período")
    private List<LinhaTempoSinaisVitaisDTO> sinaisVitais;

    @Schema(description = "Hábitos do período")
    private List<LinhaTempoHabitosDTO> habitos;

    @Schema(description = "Sintomas do período")
    private List<LinhaTempoSintomasDTO> sintomas;


    public RelatorioPreviewDTO(RelatorioPacienteResumoDTO paciente,
                               List<LinhaTempoSinaisVitaisDTO> sinaisVitais,
                               List<LinhaTempoHabitosDTO> habitos,
                               List<LinhaTempoSintomasDTO> sintomas) {
        this.paciente = paciente;
        this.sinaisVitais = sinaisVitais;
        this.habitos = habitos;
        this.sintomas = sintomas;
    }


    public RelatorioPacienteResumoDTO getPaciente() {return paciente;}
    public void setPaciente(RelatorioPacienteResumoDTO paciente) {this.paciente = paciente;}

    public List<LinhaTempoSinaisVitaisDTO> getSinaisVitais() { return sinaisVitais; }
    public void setSinaisVitais(List<LinhaTempoSinaisVitaisDTO> sinaisVitais) {this.sinaisVitais = sinaisVitais;}

    public List<LinhaTempoHabitosDTO> getHabitos() { return habitos; }
    public void setHabitos(List<LinhaTempoHabitosDTO> habitos) { this.habitos = habitos; }

    public List<LinhaTempoSintomasDTO> getSintomas() { return sintomas; }
    public void setSintomas(List<LinhaTempoSintomasDTO> sintomas) { this.sintomas = sintomas; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    
    public LocalDateTime getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }
    
    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = List.copyOf(categorias); }
    
    public RelatorioResumoDTO getResumo() { return resumo; }
    public void setResumo(RelatorioResumoDTO resumo) { this.resumo = resumo; }
    
    @Schema(description = "Indica que não há registros no período para as categorias escolhidas", example = "false")
    public boolean isSemRegistros() {
        return sinaisVitais.isEmpty() && habitos.isEmpty() && sintomas.isEmpty();
    }

    @Schema(description = "Mensagem exibida quando não há registros, nula quando há", example = "Nenhum registro encontrado para os filtros selecionados.")
    public String getMensagem() {
        return isSemRegistros() ? "Nenhum registro encontrado para os filtros selecionados." : null;
    }
    
}
