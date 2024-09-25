package com.mercurio.lms.ppd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PPD_GRUPOS_ATENDIMENTOS")
@SequenceGenerator(name = "GRUPO_ATENDIMENTO_SEQ", sequenceName = "PPD_GRUPOS_ATENDIMENTOS_SQ")
public class PpdGrupoAtendimento implements Serializable  {	
	private static final long serialVersionUID = 1L;
	private Long idGrupoAtendimento;
	private String dsGrupoAtendimento;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_ATENDIMENTO_SEQ")
	@Column(name = "ID_GRUPO_ATENDIMENTO", nullable = false)
	public Long getIdGrupoAtendimento() {
		return idGrupoAtendimento;
	}

	public void setIdGrupoAtendimento(Long idGrupoAtendimento) {
		this.idGrupoAtendimento = idGrupoAtendimento;
	}
	
	@Column(name = "DS_GRUPO_ATENDIMENTO", length = 60, nullable = false)
	public String getDsGrupoAtendimento() {
		return dsGrupoAtendimento;
	}

	public void setDsGrupoAtendimento(String dsGrupoAtendimento) {
		this.dsGrupoAtendimento = dsGrupoAtendimento;
	}
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idGrupoAtendimento", this.getIdGrupoAtendimento());
		bean.put("dsGrupoAtendimento", this.getDsGrupoAtendimento());		
		return bean;
	}
}
