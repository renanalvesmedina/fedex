package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class EnderecosColetaDTO extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private String dsEndereco;
	private String nrEndereco;
	private String dsComplemento;
	private String dsBairro;
	private String nrCep;
	private String nmMunicipio;
	private String sgUnidadeFederativa;
	private String dsRota;
	private String dsRegiao;

	public String getDsEndereco() {
		return dsEndereco;
	}
	public void setDsEndereco(String dsEndereco) {
		this.dsEndereco = dsEndereco;
	}
	public String getNrEndereco() {
		return nrEndereco;
	}
	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}
	public String getDsComplemento() {
		return dsComplemento;
	}
	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}
	public String getDsBairro() {
		return dsBairro;
	}
	public void setDsBairro(String dsBairro) {
		this.dsBairro = dsBairro;
	}
	public String getNrCep() {
		return nrCep;
	}
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}
	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}
	public String getDsRota() {
		return dsRota;
	}
	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}
	public String getDsRegiao() {
		return dsRegiao;
	}
	public void setDsRegiao(String dsRegiao) {
		this.dsRegiao = dsRegiao;
	}


}
