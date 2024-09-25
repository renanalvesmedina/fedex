package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

public class SistemaBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long idSistema;

    private String nmSistema;

    private String dbOwner;
    
	public String getDbOwner() {
		return dbOwner;
	}

	public void setDbOwner(String dbOwner) {
		this.dbOwner = dbOwner;
	}

	public Long getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(Long idSistema) {
		this.idSistema = idSistema;
	}

	public String getNmSistema() {
		return nmSistema;
	}

	public void setNmSistema(String nmSistema) {
		this.nmSistema = nmSistema;
	}

	public SistemaBean() {
	}

}
