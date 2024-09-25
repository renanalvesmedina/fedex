package com.mercurio.lms.municipios.util;

public enum TipoLocalizacaoMunicipioEnum {
	CAPITAL("Capital", Long.valueOf(1)),
	GRANDE_CAPITAL("Grande Capital", Long.valueOf(2)),
	INTERIOR("Interior", Long.valueOf(3));
	
	private TipoLocalizacaoMunicipioEnum(String type, Long value) {
		this.type = type;
		this.value = value;
	}

	private String type;
	
	private Long value;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Long getValue() {
		return value;
	}
	
	public void setValue(Long value) {
		this.value = value;
	}
}