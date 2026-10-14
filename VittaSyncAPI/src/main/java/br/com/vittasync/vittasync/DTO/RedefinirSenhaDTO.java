package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Dados para redefinir a senha")
public class RedefinirSenhaDTO {

    @Schema(description = "Código de redefinição de 5 dígitos recebido por e-mail ou SMS", example = "48213")
    private String codigo;

    @Schema(description = "Nova senha do usuário", example = "NovaSenha@2026")
    private String novaSenha;


    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getNovaSenha() {return novaSenha;}
    public void setNovaSenha(String novaSenha) {this.novaSenha = novaSenha;}

}
