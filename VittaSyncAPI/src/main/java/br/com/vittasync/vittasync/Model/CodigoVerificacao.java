package br.com.vittasync.vittasync.Model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "CodigoVerificacao")
public class CodigoVerificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "codigo", nullable = false, length = 5) // no SQL é VARCHAR(5)
    private String codigo;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "expira", nullable = false)
    private LocalDateTime expira;

    @Column(name = "utilizado", nullable = false)
    private Boolean utilizado = false;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getExpira() { return expira; }
    public void setExpira(LocalDateTime expira) { this.expira = expira; }

    public Boolean getUtilizado() { return utilizado; }
    public void setUtilizado(Boolean utilizado) { this.utilizado = utilizado; }
}
