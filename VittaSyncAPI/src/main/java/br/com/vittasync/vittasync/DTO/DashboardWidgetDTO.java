package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Configuração de um widget do dashboard")
public class DashboardWidgetDTO {

    @Schema(description = "Identificador estável do widget", example = "stability")
    private String id;

    @Schema(description = "Indica se o widget deve ser exibido", example = "true")
    private Boolean visible;


    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Boolean getVisible() {return visible;}
    public void setVisible(Boolean visible) {this.visible = visible;}
}
