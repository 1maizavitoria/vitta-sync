package br.com.vittasync.vittasync.DTO;

import br.com.vittasync.vittasync.Model.RelatorioLog;
import br.com.vittasync.vittasync.Model.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RelatorioLogDTO {
    private Integer id;
    private String formato;
    private String categorias;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDateTime dataExportacao;

    private Integer usuarioId;
    private String usuarioNome;

    private Integer pacienteId;
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
