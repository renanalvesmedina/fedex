package com.mercurio.lms.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.transform.ResultTransformer;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.ReflectionUtils;

public class AliasToNestedBeanResultTransformer implements ResultTransformer {

	private Class clazz;
	
	private static final long serialVersionUID = 8066343693320256529L;

	

	public AliasToNestedBeanResultTransformer(Class clazz) {
		if(clazz == null)
			throw new IllegalArgumentException("A classe do bean não pode ser nula.");
		this.clazz = clazz;
	}



	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			throw new InfrastructureException(e);
		} 
		for (int i = 0; i < tuple.length; i++)
			ReflectionUtils.setNestedBeanPropertyValue(obj, aliases[i], tuple[i]);
		return obj;
	}



	public List transformList(List list) {
        return list;
	}

	public List transformListResult(List list) {
		List result = new ArrayList(list.size());
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			Object tuple = null;
			try {
				tuple = clazz.newInstance();
			} catch (Exception e) {
				throw new InfrastructureException(e);
			} 
			for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry entry = (Entry) iterator.next();
				Object value = entry.getValue();
				if(value != null){
					String key = ((String)entry.getKey()).replaceAll("[_]",".");
					ReflectionUtils.setNestedBeanPropertyValue(tuple, key , value);
				}
					
			}
			result.add(tuple);
		}
		return result;
	}

}
