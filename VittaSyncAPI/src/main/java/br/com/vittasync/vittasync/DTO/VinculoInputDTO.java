package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Dados para entrar com o código de convite")
public class VinculoInputDTO {

    @Schema(description = "Código de convite gerado pelo paciente", example = "A3F9C1")
    @NotBlank(message = "Código é obrigatório")
    private String codigo;

    @Schema(description = "Função do usuário em relação ao paciente, responsável: cuidador, responsavel_legal, acompanhante, contato_emergencia ou tutor, profissional de saúde: medico_principal, especialista, consultor, acompanhamento_clinico ou equipe_assistencial", example = "cuidador")
    @NotBlank(message = "Função é obrigatória")
    private String funcao;

    public VinculoInputDTO() {
    }


    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getFuncao() {return funcao;}
    public void setFuncao(String funcao) {this.funcao = funcao;}
    
}