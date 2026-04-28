package br.com.vittasync.vittasync.DTO;

import java.time.LocalTime;

public class LembreteMedicaoInputDTO {
    private String diasSemana;
    private LocalTime horario;
    private boolean ativo;

    public String getDiasSemana() { return diasSemana; }
    public void setDiasSemana(String diasSemana) { this.diasSemana = diasSemana; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
