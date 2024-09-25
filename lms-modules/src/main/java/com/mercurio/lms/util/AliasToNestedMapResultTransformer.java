package com.mercurio.lms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.transform.ResultTransformer;

import com.mercurio.adsm.framework.model.DomainValue;

public class AliasToNestedMapResultTransformer implements ResultTransformer {

	private static final long serialVersionUID = 8066343693320256529L;
	
	private static AliasToNestedMapResultTransformer instance = new AliasToNestedMapResultTransformer();

	public static AliasToNestedMapResultTransformer getInstance() {
		return instance;
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map result = new HashMap();
		for (int i = 0; i < tuple.length; i++) 
			createMap(result, aliases[i], tuple[i]);
		return result;
	}

	private void createMap(Map result, String alias, Object value) {
		if (alias != null && value != null) {
			String[] nested = alias.split("_");
			Map parent = result;
			for (int j = 0; j < nested.length - 1; j++)
				parent = createNested(parent, nested[j]);
			
			// Se o campo é um DomainValue, montar um mapa
			if (value instanceof DomainValue){
				Map map = new HashMap();
				map.put("value", ((DomainValue)value).getValue());
				map.put("status", ((DomainValue)value).getStatus());
				map.put("description", ((DomainValue)value).getDescription());		
				value = map;
			}
			parent.put(nested[nested.length - 1], value);
		}
	}

	private Map createNested(Map parent, String alias){
		Map child = (Map)parent.get(alias);
		if(child == null) {
			child = new HashMap();
			parent.put(alias, child);
		}
		return child;
	}

	public List transformListResult(List list) {
		List result = new ArrayList(list.size());
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			result.add(transformeTupleMap(map));
		}
		return result;
	}

	public Map transformeTupleMap(Map map) {
		Map tuple = new HashMap();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Entry) iterator.next();
			createMap(tuple, (String)entry.getKey(), entry.getValue());
		}
		return tuple;
	}

	public List transformList(List list) {
		return list;
	}
}
