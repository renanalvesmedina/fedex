package com.mercurio.lms.rest.vol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreUptimeConexaoRequestDTO {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("idFilial")
	private String idFilial;
	
	@JsonProperty("dhInicioChamada")
	private String dhInicioChamada;
	
	@JsonProperty("httpCode")
	private String httpCode;
	
	@JsonProperty("tempoExecucao")
	private String tempoExecucao;
	
	@JsonProperty("idEquipamento")
	private String idEquipamento;
	
	@JsonProperty("idFrota")
	private String idFrota;
	
	@JsonProperty("latitude")
	private String latitude;
	
	@JsonProperty("longitude")
	private String longitude;

	public StoreUptimeConexaoRequestDTO() {
	}

	public StoreUptimeConexaoRequestDTO(String idFilial,
			String dhInicioChamada, String httpCode, String tempoExecucao,
			String idEquipamento, String idFrota, String latitude,
			String longitude) {
		super();
		this.idFilial = idFilial;
		this.dhInicioChamada = dhInicioChamada;
		this.httpCode = httpCode;
		this.tempoExecucao = tempoExecucao;
		this.idEquipamento = idEquipamento;
		this.idFrota = idFrota;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(String idFilial) {
		this.idFilial = idFilial;
	}

	public String getDhInicioChamada() {
		return dhInicioChamada;
	}

	public void setDhInicioChamada(String dhInicioChamada) {
		this.dhInicioChamada = dhInicioChamada;
	}

	public String getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}

	public String getTempoExecucao() {
		return tempoExecucao;
	}

	public void setTempoExecucao(String tempoExecucao) {
		this.tempoExecucao = tempoExecucao;
	}

	public String getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public String getIdFrota() {
		return idFrota;
	}

	public void setIdFrota(String idFrota) {
		this.idFrota = idFrota;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
}
