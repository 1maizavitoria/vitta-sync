package br.com.vittasync.vittasync.DTO;

import jakarta.validation.constraints.NotBlank;

public class VinculoInputDTO {

    @NotBlank(message = "Código é obrigatório")
    private String codigo;

    public VinculoInputDTO() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}