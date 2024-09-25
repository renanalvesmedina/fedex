package com.mercurio.lms.util.zebra.enums;

public enum TipoImpressoraEnum {

	JATO_DE_TINTA("J","Impressora Jato de Tinta"),
	MATRICIAL("M", "Impressora Matricial"),
	LASER("L", "Impressora Laser"),
	BALANCA("B","Balança"),
	ZEBRA("E", "Impressora Zebra"),
	DATAMAX("D","Impressora Datamax"),
	PORTATIL("P", "Zebra Portátil"),
	DWS("S", "DWS Sist Comp Pesag/Cub"),
	WINDOWS("W", "Impressora do Windows");
	
	private String type;
	private String description;

	private TipoImpressoraEnum(String type, String description) {
		this.type = type;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}
}
