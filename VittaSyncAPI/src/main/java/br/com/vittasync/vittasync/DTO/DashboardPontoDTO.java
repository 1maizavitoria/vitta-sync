package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Medição de uma série do dashboard")
public class DashboardPontoDTO {

    @Schema(description = "Data e hora da medição", example = "2026-10-12T08:30:00")
    private LocalDateTime data;

    @Schema(description = "Valor medido na unidade da categoria", example = "122")
    private Number valor;

    public DashboardPontoDTO(LocalDateTime data, Number valor) {
        this.data = data;
        this.valor = valor;
    }


    public LocalDateTime getData() {return data;}

    public Number getValor() {return valor;}
    
}
