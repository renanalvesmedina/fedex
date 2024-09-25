package com.mercurio.lms.util.zebra.enums;

/**
 * @author marcelosc
 *
 */
public enum ZebraColorEnum {

	/**
	 * Representa a cor preta.
	 */
	BLACK ("B"),
	
	/**
	 * Representa a cor Branca.
	 */
	WHITE ("W");
	
	private final String value;

	/**
	 * Busca letra correspondente a cor.
	 * 
	 * @return Retorna a letra correspondente a cor selecionada.
	 */
	public String getValue() {
		return value;
	}
	
	private ZebraColorEnum(String value) {
        this.value = value;
    }
}
