package com.mercurio.lms.workflow.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="PERFIL")
@SequenceGenerator(name = "PERFIL_SQ", sequenceName = "PERFIL_SQ", allocationSize=1)
public class PerfilAprovadores implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idPerfil;
    private String dsPerfil;
    private String tpSituacao;

    @Id
    @Column(name = "ID_PERFIL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERFIL_SQ")
    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    @Column(name = "DS_PERFIL")
    public String getDsPerfil() {
        return dsPerfil;
    }

    public void setDsPerfil(String dsPerfil) {
        this.dsPerfil = dsPerfil;
    }

    @Column(name = "TP_SITUACAO")
    public String getTpSituacao() {
        return tpSituacao;
    }

    public void setTpSituacao(String tpSituacao) {
        this.tpSituacao = tpSituacao;
    }


}
