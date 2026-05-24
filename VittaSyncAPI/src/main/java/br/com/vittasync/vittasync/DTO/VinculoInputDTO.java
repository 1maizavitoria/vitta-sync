package br.com.vittasync.vittasync.DTO;

import jakarta.validation.constraints.NotBlank;

public class VinculoInputDTO {

    @NotBlank(message = "Código é obrigatório")
    private String codigo;

    @NotBlank(message = "Função é obrigatória")
    private String funcao;

    public VinculoInputDTO() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}