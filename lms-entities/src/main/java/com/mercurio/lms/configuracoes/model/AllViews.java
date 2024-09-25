package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

public class AllViews implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private String owner;
	
	private Integer id;

	private String viewName;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
