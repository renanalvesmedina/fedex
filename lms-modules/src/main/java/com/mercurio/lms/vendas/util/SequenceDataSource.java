package com.mercurio.lms.vendas.util;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
  
public class SequenceDataSource implements JRDataSource {
	private int num;
	
	
	public SequenceDataSource(int num) {
		this.num = num;
	}

	public boolean next() throws JRException {
		if (this.num-- >= 0) return true;
		return false;
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		return null;
	}

}
