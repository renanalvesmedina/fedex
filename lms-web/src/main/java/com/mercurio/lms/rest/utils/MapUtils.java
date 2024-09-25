package com.mercurio.lms.rest.utils;

import java.util.Map;

public abstract class MapUtils {
	
	private MapUtils() {
	}

	@SuppressWarnings("unchecked")
	public static String get(String key, Map<String, Object> map) {
		if (key.contains(".")) {
			Object o = map.get(key.substring(0, key.indexOf(".")));
			if (o != null && o instanceof Map) {
				return get(key.substring(key.indexOf(".")+1), (Map<String, Object>)o);
			}
			return null;
		}
		Object o = map.get(key);
		return o == null ? null : o.toString();
	}
	
}
