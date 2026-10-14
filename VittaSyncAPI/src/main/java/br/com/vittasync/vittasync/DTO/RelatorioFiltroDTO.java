package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


@Schema(description = "Filtros do relatório do paciente")
public class RelatorioFiltroDTO {
    @Schema(description = "Formato do arquivo", example = "PDF", allowableValues = {"PDF", "CSV"})
    @NotBlank(message = "Informe PDF ou CSV")
    @Pattern(regexp = "(?i)PDF|CSV", message = "Formato deve ser PDF ou CSV")
    private String formato = "PDF";
    @Schema(description = "Categorias incluídas no relatório: SINAIS, SINTOMAS ou HABITOS", example = "[\"SINAIS\", \"HABITOS\"]")
    @NotEmpty(message = "Selecione ao menos uma categoria")
    private List<@NotBlank @Pattern(regexp = "SINAIS|SINTOMAS|HABITOS",
            message = "Categoria deve ser SINAIS, SINTOMAS ou HABITOS") String> categorias;
    @Schema(description = "Data inicial do período, sem data considera desde o primeiro registro", example = "2026-10-01")
    private LocalDate dataInicio;
    @Schema(description = "Data final do período, sem data considera até o último registro", example = "2026-10-13")
    private LocalDate dataFim;
    @Schema(description = "Verdadeiro retorna os dados em JSON para pré-visualização, falso retorna o arquivo", example = "true")
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

    @Schema(hidden = true)
    @AssertTrue(message = "Data inicial deve ser anterior ou igual à data final")
    public boolean isPeriodoValido() {
        return dataInicio == null || dataFim == null || !dataInicio.isAfter(dataFim);
    }

    @Schema(hidden = true)
    @AssertTrue(message = "As datas devem estar entre os anos 1 e 9999")
    public boolean isDatasSuportadas() {
        return (dataInicio == null || (dataInicio.getYear() >= 1 && dataInicio.getYear() <= 9999))
                && (dataFim == null || (dataFim.getYear() >= 1 && dataFim.getYear() <= 9999));
    }
    
}
