package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;


@Schema(description = "Evento registrado na linha de notificações do paciente")
public class EventoPacienteOutputDTO {

    @Schema(description = "ID do evento", example = "154")
    private Long id;

    @Schema(description = "ID do paciente", example = "3")
    private Integer pacienteId;

    @Schema(description = "ID do usuário que realizou a ação", example = "5")
    private Integer usuarioId;

    @Schema(description = "Código do tipo do evento usado pelo front", example = "VITAL_SIGNS_CREATED")
    private String tipoEvento;

    @Schema(description = "Título do evento", example = "Sinais vitais registrados")
    private String titulo;

    @Schema(description = "Descrição do evento", example = "Novos sinais vitais foram registrados")
    private String descricao;

    @Schema(description = "Dados extras do evento em JSON", example = "{\"patientName\": \"Maria Souza\"}")
    private String metadata;

    @Schema(description = "Indica se o evento já foi visualizado", example = "false")
    private Boolean visualizado;

    @Schema(description = "Data e hora em que o evento foi criado", example = "2026-10-13T20:15:42")
    private Timestamp criadoEm;

    @Schema(description = "Prioridade do evento", example = "normal", allowableValues = {"normal", "alta", "critico"})
    private String prioridade;

    @Schema(description = "Nome do usuário que realizou a ação", example = "Carlos Souza")
    private String usuarioNome;

    @Schema(description = "Tipo do usuário que realizou a ação", example = "responsavel", allowableValues = {"paciente", "responsavel", "saude"})
    private String usuarioTipo;



    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Integer getPacienteId() {return pacienteId;}
    public void setPacienteId(Integer pacienteId) {this.pacienteId = pacienteId;}
    
    public Integer getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Integer usuarioId) {this.usuarioId = usuarioId;}

    public String getTipoEvento() {return tipoEvento;}
    public void setTipoEvento(String tipoEvento) {this.tipoEvento = tipoEvento;}

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public String getMetadata() {return metadata;}
    public void setMetadata(String metadata) {this.metadata = metadata;}

    public Boolean getVisualizado() {return visualizado;}
    public void setVisualizado(Boolean visualizado) {this.visualizado = visualizado;}

    public Timestamp getCriadoEm() {return criadoEm;}
    public void setCriadoEm(Timestamp criadoEm) {this.criadoEm = criadoEm;}
    
    public String getPrioridade() {return prioridade;}
    public void setPrioridade(String prioridade) {this.prioridade = prioridade;}

    public String getUsuarioNome() {return usuarioNome;}
    public void setUsuarioNome(String usuarioNome) {this.usuarioNome = usuarioNome;}

    public String getUsuarioTipo() {return usuarioTipo;}
    public void setUsuarioTipo(String usuarioTipo) {this.usuarioTipo = usuarioTipo;}

}
