package com.mercurio.lms.vendas.model.enums;

import com.mercurio.adsm.framework.model.DomainValue;

public enum DmStatusEnum {

	ATIVO("A", "Ativo"), 
	INATIVO("I", "Inativo");

	private String value;
	private String description;

	private DmStatusEnum(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public String getValue() {
		return this.value;
	}

	public DomainValue getDomainValue() {
		return new DomainValue(this.getValue());
	}

}
