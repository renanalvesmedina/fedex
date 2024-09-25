package com.mercurio.lms.util.zebra.enums;


/**
 * @author marcelosc
 *
 */
public enum ZebraTextJustifEnum {

	/**
	 * Representa o texto alinhado a esquerda.
	 */
	LEFT ("L"),
	
	/**
	 * Representa o texto centralizado.
	 */
	CENTER ("C"),
	
	/**
	 * Representa o texto alinhado a direita.
	 */
	RIGHT ("R"),
	
	/**
	 * Representa o texto justificado.
	 */
	JUSTIFIED ("J");
	
	private final String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	private ZebraTextJustifEnum(String value) {
		this.value = value;
	}
}
