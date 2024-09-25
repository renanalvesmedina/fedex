package com.mercurio.lms.workflow.model;

import java.io.Serializable;

import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class Mensagem implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMensagem;

    /** persistent field */
    private Short nrPendencia;
    
    /** persistent field */
    private Usuario usuario;

	public Long getIdMensagem() {
		return idMensagem;
	}

	public void setIdMensagem(Long idMensagem) {
		this.idMensagem = idMensagem;
	}

	public Short getNrPendencia() {
		return nrPendencia;
	}

	public void setNrPendencia(Short nrPendencia) {
		this.nrPendencia = nrPendencia;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
