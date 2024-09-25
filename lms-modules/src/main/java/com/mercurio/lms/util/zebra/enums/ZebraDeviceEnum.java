package com.mercurio.lms.util.zebra.enums;

/**
 * @author marcelosc
 *
 */
public enum ZebraDeviceEnum {

	/**
	 * Source device referente a memoria Flash da impressora.
	 */
	FLASH ("E"),
	
	/**
	 * Source device referente a memoria RAM da impressora.
	 */
	RAM ("R");
	
	private final String device;
	
	/**
	 * Busca letra do device.
	 * 
	 * @return Retorna a letra correspondente ao device selecionado.
	 */
	public String getDevice() {
		return device;
	}

	private ZebraDeviceEnum(String device) {
        this.device = device;
    }
}
