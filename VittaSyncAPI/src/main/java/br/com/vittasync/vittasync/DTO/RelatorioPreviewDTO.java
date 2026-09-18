package br.com.vittasync.vittasync.DTO;


import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class RelatorioPreviewDTO {
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDateTime dataEmissao;
    private List<String> categorias;
    private RelatorioResumoDTO resumo;
    private RelatorioPacienteResumoDTO paciente;
    private List<LinhaTempoSinaisVitaisDTO> sinaisVitais;
    private List<LinhaTempoHabitosDTO> habitos;
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


    public RelatorioPacienteResumoDTO getPaciente() { return paciente; }
    public void setPaciente(RelatorioPacienteResumoDTO paciente) { this.paciente = paciente; }

    public List<LinhaTempoSinaisVitaisDTO> getSinaisVitais() { return sinaisVitais; }
    public void setSinaisVitais(List<LinhaTempoSinaisVitaisDTO> sinaisVitais) { this.sinaisVitais = sinaisVitais; }

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
    public boolean isSemRegistros() {
        return sinaisVitais.isEmpty() && habitos.isEmpty() && sintomas.isEmpty();
    }
    public String getMensagem() {
        return isSemRegistros() ? "Nenhum registro encontrado para os filtros selecionados." : null;
    }
}
