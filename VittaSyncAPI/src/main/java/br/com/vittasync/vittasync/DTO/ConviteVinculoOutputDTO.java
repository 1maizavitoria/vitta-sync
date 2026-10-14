package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;

@Schema(description = "Código de convite gerado pelo paciente")
public class ConviteVinculoOutputDTO {

    @Schema(description = "Código de convite", example = "A3F9C1")
    private String codigo;

    @Schema(description = "Link de entrada com o código", example = "http://localhost:5173/entrar?codigo=A3F9C1")
    private String link;

    @Schema(description = "Data e hora em que o código expira", example = "2026-10-14T21:30:00")
    private Timestamp expiraEm;


    public ConviteVinculoOutputDTO() {
    }

    public String getCodigo() {return codigo;}

    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getLink() {return link;}

    public void setLink(String link) {this.link = link;}

    public Timestamp getExpiraEm() {return expiraEm;}

    public void setExpiraEm(Timestamp expiraEm) {this.expiraEm = expiraEm;}
    
}