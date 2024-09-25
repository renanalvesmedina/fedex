package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class GrupoJob implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idGrupoJob;
	
	private String nmGrupo;
	
	private String dsGrupo;

	public String getDsGrupo() {
		return dsGrupo;
	}

	public void setDsGrupo(String dsGrupo) {
		this.dsGrupo = dsGrupo;
	}

	public Long getIdGrupoJob() {
		return idGrupoJob;
	}

	public void setIdGrupoJob(Long idGrupoJob) {
		this.idGrupoJob = idGrupoJob;
	}

	public String getNmGrupo() {
		return nmGrupo;
	}

	public void setNmGrupo(String nmGrupo) {
		this.nmGrupo = nmGrupo;
	}	
}
