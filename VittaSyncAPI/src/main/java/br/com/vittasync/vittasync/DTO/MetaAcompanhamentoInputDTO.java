package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;


@Schema(description = "Dados de uma meta de acompanhamento do paciente")
public class MetaAcompanhamentoInputDTO {

    @Schema(description = "Nome da meta", example = "Perder peso até o fim do ano")
    private String nome;

    @Schema(description = "Origem dos dados da meta", example = "sinais_vitais", allowableValues = {"sinais_vitais", "habitos", "personalizado"})
    private String tipoDado;

    @Schema(description = "Indicador acompanhado pela meta", example = "peso", allowableValues = {"peso", "horas_sono", "minutos_exercicio", "personalizado"})
    private String indicador;

    @Schema(description = "Se o objetivo é aumentar ou reduzir o valor", example = "reduzir", allowableValues = {"aumentar", "reduzir"})
    private String direcao;

    @Schema(description = "Valor no início da meta, ignorado em metas personalizadas", example = "80.0")
    private Double valorInicial;

    @Schema(description = "Valor atual, ignorado em metas personalizadas e substituído pelo último registro do paciente nas demais", example = "78.5")
    private Double valorAtual;

    @Schema(description = "Unidade do valor", example = "kg")
    private String unidade;

    @Schema(description = "Valor que se deseja alcançar", example = "75.0")
    private Double valorAlvo;

    @Schema(description = "Prazo da meta", example = "2026-12-31")
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
