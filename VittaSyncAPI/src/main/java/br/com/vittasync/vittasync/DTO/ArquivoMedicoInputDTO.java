package br.com.vittasync.vittasync.DTO;

public class ArquivoMedicoInputDTO {
    private String nomeArquivo;
    private String extensao;

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getExtensao() { return extensao; }
    public void setExtensao(String extensao) { this.extensao = extensao; }

}