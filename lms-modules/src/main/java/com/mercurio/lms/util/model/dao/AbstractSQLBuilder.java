package com.mercurio.lms.util.model.dao;

import java.util.Map;

public abstract class AbstractSQLBuilder {
	protected abstract String getSQL();
	protected abstract Map<String, Object> getParametros();
	
	public ConsultaSQL build() {
		return new ConsultaSQL(getSQL(), getParametros());
	}
}
