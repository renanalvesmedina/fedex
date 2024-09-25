package com.mercurio.lms.util.zebra.enums;


/**
 * @author marcelosc
 *
 */
public enum ZebraPrinterResEnum {

	/**
	 * Corresponde a uma impressora com 8 dots por mm.
	 */
	DOTS_PER_MM_8 (8),
	
	/**
	 * Corresponde a uma impressora com 12 dots por mm.
	 */
	DOTS_PER_MM_12 (12),
	
	/**
	 * Corresponde a uma impressora com 24 dots por mm.
	 */
	DOTS_PER_MM_24 (24);
	
	private final int dotsPerMm;
	
	/**
	 * @return the dotsPerMm corresponding.
	 */
	public int getDotsPerMm() {
		return dotsPerMm;
	}

	private ZebraPrinterResEnum(int dotsPerMm) {
		this.dotsPerMm = dotsPerMm;
	}
}
