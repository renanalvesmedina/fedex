package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

public class IdiomaXMLBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private VarcharI18n ds;
	
	public VarcharI18n  getDs() {
		return ds;
	}

	public void setDs(VarcharI18n  ds) {
		this.ds = ds;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
