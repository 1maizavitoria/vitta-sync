package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "PreferenciaDashboard",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_preferencia_dashboard_usuario",
                columnNames = "usuario_id"
        )
)
public class PreferenciaDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer versao;

    @Lob
    @Column(name = "layout_json", nullable = false, columnDefinition = "TEXT")
    private String layoutJson;

    @Column(name = "data_modificacao", nullable = false)
    private LocalDateTime dataModificacao;


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}

    public Integer getVersao() {return versao;}
    public void setVersao(Integer versao) {this.versao = versao;}

    public String getLayoutJson() {return layoutJson;}
    public void setLayoutJson(String layoutJson) {this.layoutJson = layoutJson;}

    public LocalDateTime getDataModificacao() {return dataModificacao;}
    public void setDataModificacao(LocalDateTime dataModificacao) {this.dataModificacao = dataModificacao;}
}
