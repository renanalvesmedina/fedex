package com.mercurio.lms.configuracoes.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.lms.configuracoes.model.IdiomaBean;

public class IdiomaMapper implements RowMapper{

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		IdiomaBean bean = new IdiomaBean();
		bean.setIdPortugues(rs.getInt("ID_PORT"));
		bean.setDescricaoPortugues(rs.getString("DS_PORT"));
		bean.setDescricaoTraduzida(rs.getString("DS_ESTR"));
		bean.setTamanhoColuna(rs.getInt("COLUMN_LENGTH"));
		return bean;
	}
}
