package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "Categoria do dashboard com as suas séries de medições")
public class DashboardCategoriaDTO {

    @Schema(description = "Código da categoria", example = "pressao")
    private String codigo;

    @Schema(description = "Nome de exibição da categoria", example = "Pressão arterial")
    private String nome;

    @Schema(description = "Unidade de medida da categoria", example = "mmHg")
    private String unidade;

    @Schema(description = "Séries da categoria, a pressão tem duas: sistólica e diastólica")
    private List<DashboardSerieDTO> series;

    public DashboardCategoriaDTO(
            String codigo,
            String nome,
            String unidade,
            List<DashboardSerieDTO> series
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.unidade = unidade;
        this.series = series;
    }


    public String getCodigo() {return codigo;}

    public String getNome() {return nome;}

    public String getUnidade() {return unidade;}

    public List<DashboardSerieDTO> getSeries() {return series;}
}
