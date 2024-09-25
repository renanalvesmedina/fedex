package com.mercurio.lms.configuracoes.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.lms.configuracoes.model.SistemaBean;

public class SistemaMapper implements RowMapper{

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SistemaBean sistema = new SistemaBean();
		sistema.setIdSistema(rs.getLong("ID_SISTEMA"));
		sistema.setNmSistema(rs.getString("NM_SISTEMA"));
		sistema.setDbOwner(rs.getString("DB_OWNER"));
		return sistema;
	}

}
