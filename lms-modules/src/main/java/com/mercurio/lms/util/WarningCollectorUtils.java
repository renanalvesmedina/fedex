package com.mercurio.lms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.session.SessionContext;

public abstract class WarningCollectorUtils {
	private static final String SESSION_WARNINGS = "SESSION_WARNINGS"; 

	public static void putAll(Map resultMap) {
		List<String> warnings = (List)SessionContext.get(SESSION_WARNINGS);
		if(warnings != null) {
			List<Map> results = new ArrayList<Map>();
			for (String warning : warnings) {
				Map warningMap = new HashMap();
				warningMap.put("warning", warning);
				results.add(warningMap);
			}
			resultMap.put("warnings", results);
			remove();
		}
	}

	public static void remove() {
		SessionContext.set(SESSION_WARNINGS, null);
	}

	public static Boolean existWarnings() {
		List<String> warnings = (List)SessionContext.get(SESSION_WARNINGS);
		return (warnings != null && warnings.size() > 0);
	}
}
