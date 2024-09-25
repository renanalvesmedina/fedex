package com.mercurio.lms.vendas.util;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class MultiSequenceDataSource implements JRDataSource {
	private Iterator it;
	private JRDataSource dataSource;
	
	
	public MultiSequenceDataSource(List dataSources) {
		it = dataSources.iterator();
		dataSource = (JRDataSource)it.next();
	}

	public boolean next() throws JRException {
		if (dataSource.next()) return true;
		while (it.hasNext()) {
			dataSource = (JRDataSource)it.next();
			if (dataSource.next()) return true;
		}
		
		return false;
	}

	public Object getFieldValue(JRField arg0) throws JRException {
		return dataSource.getFieldValue(arg0);
	}
}
