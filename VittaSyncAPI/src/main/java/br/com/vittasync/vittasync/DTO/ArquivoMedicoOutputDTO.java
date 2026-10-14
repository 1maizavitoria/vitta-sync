package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;


@Schema(description = "Dados de um documento médico enviado")
public class ArquivoMedicoOutputDTO {
    @Schema(description = "ID do documento", example = "8")
    private Integer id;

    @Schema(description = "Nome de exibição do documento", example = "Hemograma completo")
    private String nomeArquivo;

    @Schema(description = "Data e hora em que o documento foi enviado", example = "2026-10-10T14:22:05")
    private LocalDateTime dataUpload;

    @Schema(description = "CPF do paciente", example = "52998224725")
    private String pacienteCpf;

    @Schema(description = "Extensão do arquivo", example = ".pdf")
    private String extensao;

    @Schema(description = "Nome original do arquivo enviado", example = "hemograma_outubro.pdf")
    private String nomeOriginal;

    @Schema(description = "Nome do profissional de saúde que enviou o documento", example = "Dra. Fernanda Lopes")
    private String medicoNome;

    @Schema(description = "Nome do paciente, preenchido apenas na listagem do profissional", example = "Maria Oliveira")
    private String pacienteNome;

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }

    public String getMedicoNome() {
        return medicoNome;
    }

    public void setMedicoNome(String medicoNome) {
        this.medicoNome = medicoNome;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public void setNomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public String getPacienteCpf() {
        return pacienteCpf;
    }

    public void setPacienteCpf(String pacienteCpf) {
        this.pacienteCpf = pacienteCpf;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }
}