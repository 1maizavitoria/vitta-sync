package br.com.vittasync.vittasync.DTO;

import java.time.LocalTime;

public class LembreteMedicaoOutputDTO {
    private Long id;
    private String diasSemana;
    private LocalTime horario;
    private boolean ativo;

    public LembreteMedicaoOutputDTO(Long id, String diasSemana, LocalTime horario, boolean ativo) {
        this.id = id;
        this.diasSemana = diasSemana;
        this.horario = horario;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getDiasSemana() { return diasSemana; }
    public LocalTime getHorario() { return horario; }
    public boolean isAtivo() { return ativo; }
}
