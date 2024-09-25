package com.mercurio.lms.util.zebra.enums;

/**
 * @author marcelosc
 *
 */
public enum ZebraFontEnum {

	FONT_0 ("0", 12, 15),
	
	FONT_D ("D", 10, 18),
	
	FONT_P ("P", 18, 20),
	
	FONT_Q ("Q", 24, 28),
	
	FONT_R ("R", 31, 35),
	
	FONT_S ("S", 35, 40),
	
	FONT_T ("T", 42, 48),
	
	FONT_U ("U", 53, 58),
	
	FONT_V ("V", 71, 80);
	
	private final String name;
	
	private final int height;
	
	private final int width;

	private ZebraFontEnum(String name, int width, int height) {
		this.height = height;
		this.name = name;
		this.width = width;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	
}
