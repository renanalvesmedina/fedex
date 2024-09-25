package com.mercurio.lms.configuracoes.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.core.util.VarcharI18nUtil;
import com.mercurio.lms.configuracoes.model.IdiomaXMLBean;

public class IdiomaXMLMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		IdiomaXMLBean bean = new IdiomaXMLBean();
		bean.setId(rs.getInt("id"));
		bean.setDs(VarcharI18nUtil.createVarcharI18n(rs.getObject("ds")));
		return bean;
	}

}
