package com.mercurio.lms.rest.vol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class FindEntregasRequestDTO {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("idControleCarga")
	@NotNull
	private String idControleCarga;
	
	@JsonProperty("idEquipamento")
	private String idEquipamento;
	
	public FindEntregasRequestDTO() {		
	}
	
	public FindEntregasRequestDTO(String idControleCarga, String idEquipamento) {
		this.idControleCarga = idControleCarga;
		this.idEquipamento = idEquipamento;
	}

	public String getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(String idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public String getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
	}
	
}
