package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

public class AllTabColumns implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private String owner;

	private String tableName;
	
	private Integer idColumnName;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String columnName) {
		this.name = columnName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getIdColumnName() {
		return idColumnName;
	}

	public void setIdColumnName(Integer idColumnName) {
		this.idColumnName = idColumnName;
	}

}
