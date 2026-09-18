package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


public class RelatorioFiltroDTO {
    @NotBlank(message = "Informe PDF ou CSV")
    @Pattern(regexp = "(?i)PDF|CSV", message = "Formato deve ser PDF ou CSV")
    private String formato = "PDF";
    @NotEmpty(message = "Selecione ao menos uma categoria")
    private List<@NotBlank @Pattern(regexp = "SINAIS|SINTOMAS|HABITOS",
            message = "Categoria deve ser SINAIS, SINTOMAS ou HABITOS") String> categorias;
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

    @AssertTrue(message = "Data inicial deve ser anterior ou igual à data final")
    public boolean isPeriodoValido() {
        return dataInicio == null || dataFim == null || !dataInicio.isAfter(dataFim);
    }

    @AssertTrue(message = "As datas devem estar entre os anos 1 e 9999")
    public boolean isDatasSuportadas() {
        return (dataInicio == null || (dataInicio.getYear() >= 1 && dataInicio.getYear() <= 9999))
                && (dataFim == null || (dataFim.getYear() >= 1 && dataFim.getYear() <= 9999));
    }
}
