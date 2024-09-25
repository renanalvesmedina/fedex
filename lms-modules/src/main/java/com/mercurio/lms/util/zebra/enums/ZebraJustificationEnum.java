package com.mercurio.lms.util.zebra.enums;

/**
 * @author marcelosc
 *
 */
public enum ZebraJustificationEnum {

	/**
	 * Alinhamento a direita.
	 */
	LEFT (0),
	
	/**
	 * Alinhamento a esquerda.
	 */
	RIGHT (1);

	private final int value;
	
	/**
	 * @return the value of the enum.
	 */
	public int getValue() {
		return value;
	}

	private ZebraJustificationEnum(int value) {
		this.value = value;
	}
}
