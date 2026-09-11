package br.com.vittasync.vittasync.DTO;


import java.util.List;


public class LinhaTempoResponseDTO {
    private List<LinhaTempoSinaisVitaisDTO> sinaisVitais;
    private List<LinhaTempoHabitosDTO> habitos;
    private List<LinhaTempoSintomasDTO> sintomas;
    private List<LinhaTempoItemDTO> itens;

    public LinhaTempoResponseDTO(List<LinhaTempoSinaisVitaisDTO> sinaisVitais,
                                 List<LinhaTempoHabitosDTO> habitos,
                                 List<LinhaTempoSintomasDTO> sintomas,
                                 List<LinhaTempoItemDTO> itens) {
        this.sinaisVitais = sinaisVitais;
        this.habitos = habitos;
        this.sintomas = sintomas;
        this.itens = itens;
    }


    public List<LinhaTempoSinaisVitaisDTO> getSinaisVitais() { return sinaisVitais; }
    public List<LinhaTempoHabitosDTO> getHabitos() { return habitos; }
    public List<LinhaTempoSintomasDTO> getSintomas() { return sintomas; }
    public List<LinhaTempoItemDTO> getItens() { return itens; }
}
