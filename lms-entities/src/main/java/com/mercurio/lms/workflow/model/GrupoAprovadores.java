package com.mercurio.lms.workflow.model;

import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="PERFIL_USUARIO")
@SequenceGenerator(name = "PERFIL_USUARIO_SQ", sequenceName = "PERFIL_USUARIO_SQ", allocationSize=1)
public class GrupoAprovadores implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idPerfilUsuario;
    private PerfilAprovadores perfil;
    private UsuarioADSM usuario;
    private String tpSituacao;

    @Id
    @Column(name = "ID_PERFIL_USUARIO", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERFIL_USUARIO_SQ")
    public Long getIdPerfilUsuario() {
        return idPerfilUsuario;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERFIL", nullable = false)
    public PerfilAprovadores getPerfil() {
        return perfil;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    public UsuarioADSM getUsuario() {
        return usuario;
    }

    @Column(name = "TP_SITUACAO")
    public String getTpSituacao() {
        return tpSituacao;
    }

    public void setIdPerfilUsuario(Long idPerfilUsuario) {
        this.idPerfilUsuario = idPerfilUsuario;
    }

    public void setPerfil(PerfilAprovadores perfil) {
        this.perfil = perfil;
    }

    public void setUsuario(UsuarioADSM usuario) {
        this.usuario = usuario;
    }

    public void setTpSituacao(String tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @Override
    public String toString() {
        return "GrupoAprovadores{" +
                "idPerfilUsuario=" + idPerfilUsuario +
                ", perfil=" + perfil +
                ", usuario=" + usuario +
                ", tpSituacao='" + tpSituacao + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrupoAprovadores that = (GrupoAprovadores) o;
        return idPerfilUsuario.equals(that.idPerfilUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPerfilUsuario);
    }


}
