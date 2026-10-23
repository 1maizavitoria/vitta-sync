package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "Preferência de organização e visibilidade dos widgets do dashboard")
public class DashboardLayoutDTO {

    @Schema(description = "Versão do formato da preferência", example = "1")
    private Integer version;

    @Schema(description = "Widgets na ordem escolhida pelo usuário")
    private List<DashboardWidgetDTO> widgets;


    public Integer getVersion() {return version;}
    public void setVersion(Integer version) {this.version = version;}

    public List<DashboardWidgetDTO> getWidgets() {return widgets;}
    public void setWidgets(List<DashboardWidgetDTO> widgets) {this.widgets = widgets;}
}
