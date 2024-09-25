package com.mercurio.lms.util.model.dao;

import java.util.Map;

public class ConsultaSQL {
	private final String sql;
	private final Map<String, Object> parametros;
	
	public ConsultaSQL(String sql, Map<String, Object> parametros) {
		this.sql = sql;
		this.parametros = parametros;
	}
	
	public String getSql() {
		return sql;
	}
	
	public Map<String, Object> getParametros() {
		return parametros;
	}
}
