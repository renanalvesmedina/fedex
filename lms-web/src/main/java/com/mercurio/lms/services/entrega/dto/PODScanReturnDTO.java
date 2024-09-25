package com.mercurio.lms.services.entrega.dto;

import java.io.Serializable;

public class PODScanReturnDTO implements Serializable {
	
   private static final long serialVersionUID = 1L;
	
    private String message;	
	private String versaoApk;
	
	public PODScanReturnDTO(String message, String versaoApk) {
		this.message = message;
		this.versaoApk = versaoApk;
	}
	public String getVersaoApk() {
		return versaoApk;
	}

	public void setVersaoApk(String versaoApk) {
		this.versaoApk = versaoApk;
	}  
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}
