package br.com.vittasync.vittasync.DTO;

import java.util.List;

public class DashboardSerieDTO {

    private String codigo;
    private String nome;
    private List<DashboardPontoDTO> pontos;

    public DashboardSerieDTO(String codigo, String nome, List<DashboardPontoDTO> pontos) {
        this.codigo = codigo;
        this.nome = nome;
        this.pontos = pontos;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public List<DashboardPontoDTO> getPontos() {
        return pontos;
    }
}
