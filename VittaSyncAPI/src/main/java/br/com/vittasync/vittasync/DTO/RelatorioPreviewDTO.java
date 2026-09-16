package br.com.vittasync.vittasync.DTO;


import java.util.List;


public class RelatorioPreviewDTO {
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
}
