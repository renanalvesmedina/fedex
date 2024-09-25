package com.mercurio.lms.vendas.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * 
 * @author maximilianog
 * @deprecated
 */
public class ListDataSource implements JRDataSource {
	
	private Iterator[] it;
	private Map map;
	private int tamanho = 0;
	private int posGrp=0;
	
	
	public ListDataSource(List list) {
		it = new Iterator[list.size()];
		for (int i=0 ; i < it.length; i++) {
			it[i] = ((List)list.get(i)).iterator();
			tamanho++;
		}
	}

	public boolean next() throws JRException {
		if (posGrp < it.length && it[posGrp] != null && it[posGrp].hasNext()) {
			this.map = (Map)it[posGrp].next();
			return true;
		} else {
			posGrp++;
		}
		return false;
	}

	public Object getFieldValue(JRField arg0) throws JRException {
		
		return this.map.get(arg0.getName());
	}
}
