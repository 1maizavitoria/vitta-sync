package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;
import java.util.List;


public class RelatorioFiltroDTO {
    private String formato = "PDF";
    private List<String> categorias;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean preview = false;


    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public boolean isPreview() { return preview; }
    public void setPreview(boolean preview) { this.preview = preview; }
}
