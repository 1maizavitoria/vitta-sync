package br.com.vittasync.vittasync.DTO;

import java.util.List;

public class DashboardCategoriaDTO {

    private String codigo;
    private String nome;
    private String unidade;
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

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public List<DashboardSerieDTO> getSeries() {
        return series;
    }
}
