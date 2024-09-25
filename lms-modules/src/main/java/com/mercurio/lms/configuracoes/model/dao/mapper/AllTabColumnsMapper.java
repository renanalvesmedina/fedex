package com.mercurio.lms.configuracoes.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.lms.configuracoes.model.AllTabColumns;

public class AllTabColumnsMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		AllTabColumns allTabColumns = new AllTabColumns();
		allTabColumns.setIdColumnName(rs.getString("COLUMN_NAME").hashCode());
		allTabColumns.setName(rs.getString("COLUMN_NAME"));
		return allTabColumns;
	}

}
