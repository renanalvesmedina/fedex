package com.mercurio.lms.services.vol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecuteControleEquipamentoRequestDTO {

	private static final long serialVersionUID = 1L;

	@JsonProperty("idFilial")
	private String idFilial;

	@JsonProperty("imei")
	private String imei;
	
	@JsonProperty("dhRetirada")
	private String dhRetirada;
	
	@JsonProperty("idMotorista")
	private String idMotorista;
	
	@JsonProperty("idFrota")
	private String idFrota;

	public ExecuteControleEquipamentoRequestDTO() {
	}

	public ExecuteControleEquipamentoRequestDTO(String idFilial, String imei,
			String dhRetirada, String idMotorista, String idFrota) {
		super();
		this.idFilial = idFilial;
		this.imei = imei;
		this.dhRetirada = dhRetirada;
		this.idMotorista = idMotorista;
		this.idFrota = idFrota;
	}

	public String getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(String idFilial) {
		this.idFilial = idFilial;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getDhRetirada() {
		return dhRetirada;
	}

	public void setDhRetirada(String dhRetirada) {
		this.dhRetirada = dhRetirada;
	}

	public String getIdMotorista() {
		return idMotorista;
	}

	public void setIdMotorista(String idMotorista) {
		this.idMotorista = idMotorista;
	}

	public String getIdFrota() {
		return idFrota;
	}

	public void setIdFrota(String idFrota) {
		this.idFrota = idFrota;
	}

}
