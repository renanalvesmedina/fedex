package com.mercurio.lms.configuracoes.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.lms.configuracoes.model.AllViews;

public class AllViewsMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		AllViews allViews = new AllViews();
		allViews.setViewName(rs.getString("VIEW_NAME"));
		allViews.setId(rs.getString("VIEW_NAME").hashCode());
		return allViews;
	}

}
