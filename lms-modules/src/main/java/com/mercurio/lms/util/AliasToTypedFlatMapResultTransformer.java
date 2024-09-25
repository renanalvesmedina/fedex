package com.mercurio.lms.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.transform.ResultTransformer;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;

public class AliasToTypedFlatMapResultTransformer implements ResultTransformer {

	private static final long serialVersionUID = 8066343693320256529L;
	
	private static AliasToTypedFlatMapResultTransformer instance = new AliasToTypedFlatMapResultTransformer();

	public static AliasToTypedFlatMapResultTransformer getInstance() {
		return instance;
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		TypedFlatMap result = new TypedFlatMap();
		for (int i = 0; i < tuple.length; i++) 
			createMap(result, aliases[i], tuple[i]);
		return result;
	}

	private void createMap(TypedFlatMap result, String alias, Object value) {
		if (alias != null && value != null) {
			String key = alias.replaceAll("[_]",".");
			// Se o campo é um DomainValue, montar um mapa
			if (value instanceof DomainValue){
				result.put(key + ".value", ((DomainValue)value).getValue());
				result.put(key + ".status", ((DomainValue)value).getStatus());
				result.put(key + ".description", ((DomainValue)value).getDescription());		
			} else
				result.put(key, value);
		}
	}

	public List transformListResult(List list) {
		List result = new ArrayList(list.size());
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			result.add(transformeTupleMap(map));
		}
		return result;
	}

	public ResultSetPage transformResultSetPage(ResultSetPage rsp) {
		rsp.setList(transformListResult(rsp.getList()));
		return rsp;
	}
	
	public TypedFlatMap transformeTupleMap(Map map) {
		TypedFlatMap tuple = new TypedFlatMap();
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
