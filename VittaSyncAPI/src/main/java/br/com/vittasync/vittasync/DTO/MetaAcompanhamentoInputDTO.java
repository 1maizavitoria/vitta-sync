package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;


public class MetaAcompanhamentoInputDTO {

    private String nome;
    private String tipoDado;
    private String indicador;
    private String direcao;
    private Double valorInicial;
    private Double valorAtual;
    private String unidade;
    private Double valorAlvo;
    private LocalDate dataLimite;


    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipoDado() { return tipoDado; }
    public void setTipoDado(String tipoDado) { this.tipoDado = tipoDado; }

    public String getIndicador() { return indicador; }
    public void setIndicador(String indicador) { this.indicador = indicador; }

    public String getDirecao() { return direcao; }
    public void setDirecao(String direcao) { this.direcao = direcao; }

    public Double getValorInicial() { return valorInicial; }
    public void setValorInicial(Double valorInicial) { this.valorInicial = valorInicial; }

    public Double getValorAtual() { return valorAtual; }
    public void setValorAtual(Double valorAtual) { this.valorAtual = valorAtual; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public Double getValorAlvo() { return valorAlvo; }
    public void setValorAlvo(Double valorAlvo) { this.valorAlvo = valorAlvo; }

    public LocalDate getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }
}
