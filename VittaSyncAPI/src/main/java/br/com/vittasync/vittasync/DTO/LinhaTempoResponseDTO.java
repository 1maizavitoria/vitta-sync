package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "Linha do tempo do paciente no período")
public class LinhaTempoResponseDTO {
    @Schema(description = "Sinais vitais do período")
    private List<LinhaTempoSinaisVitaisDTO> sinaisVitais;

    @Schema(description = "Hábitos do período")
    private List<LinhaTempoHabitosDTO> habitos;

    @Schema(description = "Sintomas do período")
    private List<LinhaTempoSintomasDTO> sintomas;

    @Schema(description = "Todos os registros juntos, do mais recente ao mais antigo")
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
