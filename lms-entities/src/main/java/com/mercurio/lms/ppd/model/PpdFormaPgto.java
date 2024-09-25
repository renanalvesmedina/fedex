package com.mercurio.lms.ppd.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PPD_FORMAS_PGTO")
@SequenceGenerator(name = "FORMA_PGTO_SEQ", sequenceName = "PPD_FORMAS_PGTO_SQ")
public class PpdFormaPgto implements Serializable  {
	private static final long serialVersionUID = 1L;
	private Long idFormaPgto;
	private String dsFormaPgto;
	private String cdFormaPgto;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORMA_PGTO_SEQ")
	@Column(name = "ID_FORMA_PGTO", nullable = false)
	public Long getIdFormaPgto() {
		return idFormaPgto;
	}

	public void setIdFormaPgto(Long idFormaPgto) {
		this.idFormaPgto = idFormaPgto;
	}
	
	@Column(name = "DS_FORMA_PGTO", nullable = false)
	public String getDsFormaPgto() {
		return dsFormaPgto;
	}

	public void setDsFormaPgto(String dsFormaPgto) {
		this.dsFormaPgto = dsFormaPgto;
	}
	
	@Column(name = "CD_FORMA_PGTO", nullable = false)
	public String getCdFormaPgto() {
		return cdFormaPgto;
	}

	public void setCdFormaPgto(String cdFormaPgto) {
		this.cdFormaPgto = cdFormaPgto;
	}
	
}
