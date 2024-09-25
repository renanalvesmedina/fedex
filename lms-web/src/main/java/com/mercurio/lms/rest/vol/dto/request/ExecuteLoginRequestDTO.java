package com.mercurio.lms.rest.vol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ExecuteLoginRequestDTO {

	private static final long serialVersionUID = 1L;

	@JsonProperty("filial")
	@NotNull
	private String filial;

	@JsonProperty("senha")
	@NotNull
	private String senha;

	@JsonProperty("controleCarga")
	@NotNull
	private String controleCarga;

	@JsonProperty("versao")
	private String versao;

	@JsonProperty("versaoSO")
	private String versaoSO;

	@JsonProperty("dataHoraCelular")
	private String dataHoraCelular;

	@JsonProperty("imei")
	@NotNull
	private String imei;

	public ExecuteLoginRequestDTO() {
	}

	public ExecuteLoginRequestDTO(String filial, String senha,
			String controleCarga, String versao, String versaoSO,
			String dataHoraCelular, String imei, String service) {
		super();
		this.filial = filial;
		this.senha = senha;
		this.controleCarga = controleCarga;
		this.versao = versao;
		this.versaoSO = versaoSO;
		this.dataHoraCelular = dataHoraCelular;
		this.imei = imei;
	}

	public String getFilial() {
		return filial;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(String controleCarga) {
		this.controleCarga = controleCarga;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getVersaoSO() {
		return versaoSO;
	}

	public void setVersaoSO(String versaoSO) {
		this.versaoSO = versaoSO;
	}

	public String getDataHoraCelular() {
		return dataHoraCelular;
	}

	public void setDataHoraCelular(String dataHoraCelular) {
		this.dataHoraCelular = dataHoraCelular;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
