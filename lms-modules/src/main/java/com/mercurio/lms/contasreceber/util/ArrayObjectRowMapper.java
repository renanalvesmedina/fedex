package com.mercurio.lms.contasreceber.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ArrayObjectRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Object[] elements = new Object[columnCount];
		if (columnCount == 1){
			return getColumnValue(rs, rsmd, 1);
		} else {
			for (int i = 1; i <= columnCount; i++) {
				elements[i-1] = getColumnValue(rs, rsmd, i);
			}
			return elements;
		}
		
	}
	
	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation uses the <code>getObject</code> method.
	 * Additionally, this implementation includes a "hack" to get around Oracle
	 * returning a non standard object for their TIMESTAMP datatype.
	 * @param rs is the ResultSet holding the data
	 * @param index is the column index
	 * @return the Object returned
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue
	 */
	protected Object getColumnValue(ResultSet rs, ResultSetMetaData rsmd, int index) throws SQLException {
		if (rsmd.getColumnType(index) == 2 && rsmd.getScale(index) <= 0){
			if (rs.getObject(index) != null){
				return (Long)((BigDecimal)rs.getObject(index)).longValue();
			}
		}		
		if (rsmd.getColumnType(index) == 3){
			return rs.getObject(index);
		}
		return rs.getObject(index);
	}	

}
