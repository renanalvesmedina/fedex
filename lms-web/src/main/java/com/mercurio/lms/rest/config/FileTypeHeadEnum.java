package com.mercurio.lms.rest.config;

import com.mercurio.adsm.core.InfrastructureException;

public enum FileTypeHeadEnum {
	CSV("csv","text/csv"),
	XLS("xls", "application/vnd.ms-excel"),
	PDF("pdf", "application/pdf");

	private String type;
	private String value;

	private FileTypeHeadEnum(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	
	public static String getTypeHead(String type) {
		for (FileTypeHeadEnum enumType : FileTypeHeadEnum.values()) {
			if (enumType.getType().equalsIgnoreCase(type)) {
				return enumType.getValue();
			}
		}
		throw new InfrastructureException("valorInvalido");
	}
}
