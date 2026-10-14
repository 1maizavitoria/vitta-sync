package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "Série de medições de uma categoria do dashboard")
public class DashboardSerieDTO {

    @Schema(description = "Código da série", example = "sistolica")
    private String codigo;

    @Schema(description = "Nome de exibição da série", example = "Sistólica")
    private String nome;

    @Schema(description = "Medições da série em ordem cronológica")
    private List<DashboardPontoDTO> pontos;

    public DashboardSerieDTO(String codigo, String nome, List<DashboardPontoDTO> pontos) {
        this.codigo = codigo;
        this.nome = nome;
        this.pontos = pontos;
    }


    public String getCodigo() {return codigo;}

    public String getNome() {return nome;}

    public List<DashboardPontoDTO> getPontos() {return pontos;}
    
}
