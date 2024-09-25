package com.mercurio.lms.util.enums.edw;

public enum CalendarioEDWServiceEnum {

	FIND_BY_DIA("calendariotnt/findByDia");
	
	private String path;

	private CalendarioEDWServiceEnum(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
