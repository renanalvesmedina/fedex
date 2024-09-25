package com.mercurio.lms.vendas.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class GroupDataSource implements JRDataSource {
	private Iterator it;
	private JRDataSource dataSource;
		
	public GroupDataSource(Collection list) {
		if (list == null || list.isEmpty()) return;
		it = list.iterator(); 
		dataSource = (JRDataSource)it.next();
	}
	
	public GroupDataSource(Class clazz, Collection dados, int num)  {
		if (dados == null || dados.isEmpty()) return;
		
		List list = new ArrayList();
		
		try {
			for (int i =0; i < num; i++) {
				Constructor ctr = clazz.getConstructor(new Class[]{Collection.class});
				JRDataSource tmpDS = (JRDataSource)ctr.newInstance(new Object[]{ dados });
				list.add(tmpDS);
			}
		} catch (Exception ex) {
			new IllegalArgumentException("A classe " + clazz.getName() + " deve possuir um construtor que recebe um List.");
		}
		
		it = list.iterator();
		if (it.hasNext()){
			dataSource = (JRDataSource)it.next();
		}
	}
	
	public boolean next() throws JRException {		
		if (dataSource==null) return false;		
		if (dataSource.next()) return true;
		if (it.hasNext()) dataSource = (JRDataSource)it.next();
		return false;
	}

	public Object getFieldValue(JRField arg0) throws JRException {
		if (dataSource != null) return dataSource.getFieldValue(arg0);
		return null;
	}
}
