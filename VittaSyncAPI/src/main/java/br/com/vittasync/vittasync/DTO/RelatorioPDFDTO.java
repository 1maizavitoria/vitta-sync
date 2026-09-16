package br.com.vittasync.vittasync.DTO;

public class RelatorioPDFDTO {
    private byte[] arquivo;

    public RelatorioPDFDTO(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public byte[] getArquivo() { return arquivo; }
}
