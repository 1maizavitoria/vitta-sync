package br.com.vittasync.vittasync.DTO;

import java.sql.Timestamp;

public class ConviteVinculoOutputDTO {

    private String codigo;

    private String link;

    private Timestamp expiraEm;

    public ConviteVinculoOutputDTO() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Timestamp expiraEm) {
        this.expiraEm = expiraEm;
    }
}