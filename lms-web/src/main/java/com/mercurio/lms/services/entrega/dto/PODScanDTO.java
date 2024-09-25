package com.mercurio.lms.services.entrega.dto;

import java.io.Serializable;

public class PODScanDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String barcode;
	private String media;
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}	
	

}
