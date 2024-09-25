package com.mercurio.lms.util.zebra.enums;

/**
 * @author marcelosc
 *
 */
public enum ZebraFieldOrientationEnum {

	/**
	 * Orientacao normal.
	 */
	NORMAL ("N"),
	
	/**
	 * Rotacionado 90 graus.
	 */
	ROTATED_90_DEGREES ("R"),
	
	/**
	 * Invertido em 180 graus.
	 */
	INVERTED_180_DEGREES ("I"),
	
	/**
	 * Leitura de baixo para cima
	 */
	BOTTOM_UP ("B");
	
	private final String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	private ZebraFieldOrientationEnum(String value) {
		this.value = value;
	}
}
