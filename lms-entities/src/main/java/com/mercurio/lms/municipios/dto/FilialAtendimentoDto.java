package com.mercurio.lms.municipios.dto;

import java.io.Serializable;

public class FilialAtendimentoDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String sgFilial;
	private String nmFantasia;
	private String dsEndereco;
	private String nrEndereco;
	private String dsBairro;
	private String nmMunicipio;
	private String nmUnidadeFederativa;
	private String nrCep;
	private String nrDdd;
	private String nrTelefone;
	private String dsEmail;
	
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public String getDsEmail() {
		return dsEmail;
	}
	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}
	public String getNmFantasia() {
		return nmFantasia;
	}
	public void setNmFantasia(String nmFantasia) {
		this.nmFantasia = nmFantasia;
	}
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
	public String getNrDdd() {
		return nrDdd;
	}
	public void setNrDdd(String nrDdd) {
		this.nrDdd = nrDdd;
	}
	public String getNrTelefone() {
		return nrTelefone;
	}
	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
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
	public String getNmUnidadeFederativa() {
		return nmUnidadeFederativa;
	}
	public void setNmUnidadeFederativa(String nmUnidadeFederativa) {
		this.nmUnidadeFederativa = nmUnidadeFederativa;
	}

}
