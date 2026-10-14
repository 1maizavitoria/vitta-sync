package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import br.com.vittasync.vittasync.Model.RelatorioLog;
import br.com.vittasync.vittasync.Model.Usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "Registro de uma exportação de relatório")
public class RelatorioLogDTO {
    @Schema(description = "ID da exportação", example = "9")
    private Integer id;

    @Schema(description = "Formato exportado", example = "PDF", allowableValues = {"PDF", "CSV"})
    private String formato;

    @Schema(description = "Categorias exportadas separadas por vírgula", example = "SINAIS,HABITOS")
    private String categorias;

    @Schema(description = "Data inicial do período exportado", example = "2026-10-01")
    private LocalDate dataInicio;

    @Schema(description = "Data final do período exportado", example = "2026-10-13")
    private LocalDate dataFim;

    @Schema(description = "Data e hora da exportação", example = "2026-10-13T21:30:00")
    private LocalDateTime dataExportacao;

    @Schema(description = "ID do usuário que exportou", example = "5")
    private Integer usuarioId;

    @Schema(description = "Nome do usuário que exportou", example = "Carlos Souza")
    private String usuarioNome;

    @Schema(description = "ID do paciente", example = "3")
    private Integer pacienteId;

    @Schema(description = "Nome do paciente", example = "Maria Souza")
    private String pacienteNome;


    public RelatorioLogDTO(RelatorioLog relatorio) {
        this.id = relatorio.getId();
        this.formato = relatorio.getFormato();
        this.categorias = relatorio.getCategorias();
        this.dataInicio = relatorio.getDataInicio();
        this.dataFim = relatorio.getDataFim();
        this.dataExportacao = relatorio.getDataExportacao();

        Usuario usuario = relatorio.getUsuario();
        Usuario paciente = relatorio.getPaciente();

        if (usuario != null) {
            this.usuarioId = usuario.getId();
            this.usuarioNome = usuario.getNome();
        }
        if (paciente != null) {
            this.pacienteId = paciente.getId();
            this.pacienteNome = paciente.getNome();
        }
    }


    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getFormato() {return formato;}
    public void setFormato(String formato) {this.formato = formato;}

    public String getCategorias() {return categorias;}
    public void setCategorias(String categorias) {this.categorias = categorias;}

    public LocalDate getDataInicio() {return dataInicio;}
    public void setDataInicio(LocalDate dataInicio) {this.dataInicio = dataInicio;}

    public LocalDate getDataFim() {return dataFim;}
    public void setDataFim(LocalDate dataFim) {this.dataFim = dataFim;}

    public LocalDateTime getDataExportacao() {return dataExportacao;}
    public void setDataExportacao(LocalDateTime dataExportacao) {this.dataExportacao = dataExportacao;}

    public Integer getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Integer usuarioId) {this.usuarioId = usuarioId;}

    public String getUsuarioNome() {return usuarioNome;}
    public void setUsuarioNome(String usuarioNome) {this.usuarioNome = usuarioNome;}

    public Integer getPacienteId() {return pacienteId;}
    public void setPacienteId(Integer pacienteId) {this.pacienteId = pacienteId;}

    public String getPacienteNome() {return pacienteNome;}
    public void setPacienteNome(String pacienteNome) {this.pacienteNome = pacienteNome;}
    
}
