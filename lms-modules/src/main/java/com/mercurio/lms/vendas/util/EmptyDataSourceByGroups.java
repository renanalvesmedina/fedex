package com.mercurio.lms.vendas.util;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class EmptyDataSourceByGroups implements JRDataSource {

	private boolean hasNext = false;
	
	public boolean next() throws JRException {
		return (hasNext = !hasNext);
	}

	public Object getFieldValue(JRField arg0) throws JRException {
		// TODO Auto-generated method stub
		return null;
	}

}
