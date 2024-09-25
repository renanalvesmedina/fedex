package com.mercurio.lms.tabelaprecos.model.dao;


public class PreparedStatementDTO {

	String queryString;
	Object[] parameters;
	
	public PreparedStatementDTO(String queryString, Object[] parameters) {
		super();
		this.queryString = queryString;
		this.parameters = parameters;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
	
}
