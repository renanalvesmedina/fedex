package com.mercurio.lms.vendas.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class MultiListDataSource implements JRDataSource {

	private Iterator[] its;
	private Map mapLine;
	
	public MultiListDataSource(List values) {
		its = new Iterator[values.size()];
		for (int i = 0; i < values.size(); i++) {
			its[i] = ((List)values.get(i)).iterator();
		}		
	}
	
	public boolean next() throws JRException {
		mapLine = null;
		
		for (int i = 0; i < its.length; i++) {
			if (its[i].hasNext()) {
				if (mapLine == null) mapLine = (Map)its[i].next();
				else mapLine.putAll((Map)its[i].next());
			}
		}
		
		return (mapLine != null);
	}

	public Object getFieldValue(JRField arg0) throws JRException {
		if (mapLine != null) return mapLine.get(arg0.getName());
		return null;
	}
	
	public void add(List value) {
		Iterator[] tmp = new Iterator[its.length + 1];
		System.arraycopy(its, 0, tmp, 0, its.length);
		tmp[tmp.length - 1] = value.iterator();
		
		its = tmp;
	}

}
