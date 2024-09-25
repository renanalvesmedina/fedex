package com.mercurio.lms.util;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.session.SessionContext;

public class WarningCollector {
	private static final String SESSION_WARNINGS = "SESSION_WARNINGS"; 

	public WarningCollector(String warning) {
		List<String> warnings = (List)SessionContext.get(SESSION_WARNINGS);
		if(warnings == null) {
			warnings = new ArrayList<String>();
		}
		warnings.add(warning);
		SessionContext.set(SESSION_WARNINGS, warnings);
	}

}
