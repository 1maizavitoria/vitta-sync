package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Meta de acompanhamento do paciente")
public class MetaAcompanhamentoOutputDTO {

    @Schema(description = "ID da meta", example = "7")
    private Long id;

    @Schema(description = "Nome da meta", example = "Perder peso até o fim do ano")
    private String nome;

    @Schema(description = "Origem dos dados da meta", example = "sinais_vitais", allowableValues = {"sinais_vitais", "habitos", "personalizado"})
    private String tipoDado;

    @Schema(description = "Indicador acompanhado pela meta", example = "peso", allowableValues = {"peso", "horas_sono", "minutos_exercicio", "personalizado"})
    private String indicador;

    @Schema(description = "Se o objetivo é aumentar ou reduzir o valor", example = "reduzir", allowableValues = {"aumentar", "reduzir"})
    private String direcao;

    @Schema(description = "Valor no início da meta", example = "80.0")
    private Double valorInicial;

    @Schema(description = "Valor atual", example = "78.5")
    private Double valorAtual;

    @Schema(description = "Unidade do valor", example = "kg")
    private String unidade;

    @Schema(description = "Valor que se deseja alcançar", example = "75.0")
    private Double valorAlvo;

    @Schema(description = "Progresso da meta em porcentagem", example = "30.0")
    private Double progresso;

    @Schema(description = "Situação da meta", example = "em_andamento", allowableValues = {"em_andamento", "concluido", "concluido_atrasado", "nao_atingida"})
    private String status;

    @Schema(description = "Data e hora em que a meta foi cadastrada", example = "2026-10-01T10:20:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Prazo da meta", example = "2026-12-31")
    private LocalDate dataLimite;

    @Schema(description = "Data e hora da conclusão, nula enquanto a meta está em andamento", example = "2026-12-20T18:00:00")
    private LocalDateTime dataConclusao;

    @Schema(description = "Data e hora da última edição, nula se a meta nunca foi editada", example = "2026-10-13T21:02:10")
    private LocalDateTime dataModificacao;



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Double getProgresso() { return progresso; }
    public void setProgresso(Double progresso) { this.progresso = progresso; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDate getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }
    
}
