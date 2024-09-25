package com.mercurio.lms.contratacaoveiculos.model.util;

public enum MotoristaConstants {
	
	STATUS_ATIVO("A"),
	STATUS_INATIVO("I"),
	TIPO_VINCULO_FUNCIONARIO("F");
	
	private String value;
	
	/**
	 * 
	 * @param value
	 */
	MotoristaConstants(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
