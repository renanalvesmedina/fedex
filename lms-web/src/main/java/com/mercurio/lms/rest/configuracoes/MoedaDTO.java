package com.mercurio.lms.rest.configuracoes;

import com.mercurio.adsm.rest.BaseDTO;

public class MoedaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idMoeda;
	private String sgMoeda;
	private String dsSimbolo;
	private String siglaSimbolo;
	
	public MoedaDTO(){
		
	}
	
	public MoedaDTO(Long idMoeda, String sgMoeda, String dsSimbolo) {
		super();
		this.idMoeda = idMoeda;
		this.sgMoeda = sgMoeda;
		this.dsSimbolo = dsSimbolo;
	}

	public void setSiglaSimbolo(String siglaSimbolo) {
		this.siglaSimbolo = siglaSimbolo;
	}
	
	public String getSiglaSimbolo() {
		if (sgMoeda != null && dsSimbolo != null) { 
			return sgMoeda + " " + dsSimbolo;
		} else {
			return siglaSimbolo;
		}
	}
	
	public Long getIdMoeda() {
		return idMoeda;
	}

	public void setIdMoeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public String getSgMoeda() {
		return sgMoeda;
	}

	public void setSgMoeda(String sgMoeda) {
		this.sgMoeda = sgMoeda;
	}

	public String getDsSimbolo() {
		return dsSimbolo;
	}

	public void setDsSimbolo(String dsSimbolo) {
		this.dsSimbolo = dsSimbolo;
	}
}