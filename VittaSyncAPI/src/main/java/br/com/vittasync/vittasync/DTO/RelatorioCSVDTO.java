package br.com.vittasync.vittasync.DTO;

public class RelatorioCSVDTO {
    private byte[] arquivo;

    public RelatorioCSVDTO(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public byte[] getArquivo() { return arquivo; }
}
