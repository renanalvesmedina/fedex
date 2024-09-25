package com.mercurio.lms.configuracoes.model.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.mercurio.adsm.core.model.hibernate.AdsmConnectionProvider;

public class LmsConnectionProvider extends AdsmConnectionProvider {
	
	@Override
	public Connection getConnection() throws SQLException {
		
		Connection conn = super.getConnection();
		
		return conn;
	}
}