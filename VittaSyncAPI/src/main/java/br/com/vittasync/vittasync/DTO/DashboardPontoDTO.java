package br.com.vittasync.vittasync.DTO;

import java.time.LocalDateTime;

public class DashboardPontoDTO {

    private LocalDateTime data;
    private Number valor;

    public DashboardPontoDTO(LocalDateTime data, Number valor) {
        this.data = data;
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Number getValor() {
        return valor;
    }
}
