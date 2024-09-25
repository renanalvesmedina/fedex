package com.mercurio.lms.rest.expedicao.dto;

import java.io.Serializable;

public class AwbSuggestFilterDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String value;
	
	private String tpStatusAwb;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTpStatusAwb() {
		return tpStatusAwb;
	}

	public void setTpStatusAwb(String tpStatusAwb) {
		this.tpStatusAwb = tpStatusAwb;
	}
	
}
