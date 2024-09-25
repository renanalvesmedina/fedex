package com.mercurio.lms.rest.vol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ValidaImeiRequestDTO {

	private static final long serialVersionUID = 1L;

	@JsonProperty("cdFilial")
	@NotNull
	private String cdFilial;

	@JsonProperty("imei")
	@NotNull
	private String imei;

	public ValidaImeiRequestDTO() {
	}

	public ValidaImeiRequestDTO(String cdFilial, String imei) {
		super();
		this.cdFilial = cdFilial;
		this.imei = imei;
	}

	public String getCdFilial() {
		return cdFilial;
	}

	public void setCdFilial(String cdFilial) {
		this.cdFilial = cdFilial;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
