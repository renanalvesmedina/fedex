package com.mercurio.lms.workflow.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="USUARIO_ADSM")
@SequenceGenerator(name = "USUARIO_ADSM_SQ", sequenceName = "USUARIO_ADSM_SQ", allocationSize=1)
public class UsuarioAprovador implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuario;
    private String nmUsuario;
    private String dsEmail;
    private String login;

    @Id
    @Column(name = "ID_USUARIO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_ADSM_SQ")
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Column(name = "NM_USUARIO")
    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    @Column(name = "DS_EMAIL")
    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    @Column(name = "LOGIN")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
