package com.mercurio.lms.workflow.model.dto;

import java.io.Serializable;

public class GrupoAprovadoresDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idPerfilUsuario;
    private Long idPerfil;
    private String dsPerfil;
    private String tpCategoria;
    private Long idUsuario;
    private String nmUsuario;
    private String dsEmail;
    private String login;
    private String tpSituacao;
    private String usuarioSuggest;

    public Long getIdPerfilUsuario() {
        return idPerfilUsuario;
    }

    public void setIdPerfilUsuario(Long idPerfilUsuario) {
        this.idPerfilUsuario = idPerfilUsuario;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getDsPerfil() {
        return dsPerfil;
    }

    public void setDsPerfil(String dsPerfil) {
        this.dsPerfil = dsPerfil;
    }

    public String getTpCategoria() {
        return tpCategoria;
    }

    public void setTpCategoria(String tpCategoria) {
        this.tpCategoria = tpCategoria;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTpSituacao() {
        return tpSituacao;
    }

    public void setTpSituacao(String tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getUsuarioSuggest() {
        return usuarioSuggest;
    }

    public void setUsuarioSuggest(String usuarioSuggest) {
        this.usuarioSuggest = usuarioSuggest;
    }
}
