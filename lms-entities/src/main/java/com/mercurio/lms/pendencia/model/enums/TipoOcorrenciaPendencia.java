package com.mercurio.lms.pendencia.model.enums;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;

public enum TipoOcorrenciaPendencia {
	BLOQUEIO("B", "Bloqueio"),
	LIBERACAO("L", "Liberação");
	
	private String value;
	private String description;
	
	private TipoOcorrenciaPendencia(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public DomainValue getDomainValue() {
		return new DomainValue(value);
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean equals(String value) {
		return this.value.equals(value);
	}
	
	public boolean equals(DomainValue domainValue) {
		if(domainValue == null || StringUtils.isEmpty(domainValue.getValue())) {
			return false;
		}
		
		return this.value.equals(domainValue.getValue());
	}
}
