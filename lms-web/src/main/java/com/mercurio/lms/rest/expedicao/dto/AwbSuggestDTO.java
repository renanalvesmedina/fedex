package com.mercurio.lms.rest.expedicao.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class AwbSuggestDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	
	private Long idAwb;
	private String dsAwb;
	private String dsFormatedAwb;
	private DateTime dhEmissao;

	public Long getIdAwb() {
		return idAwb;
	}
	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}
	public String getDsAwb() {
		return dsAwb;
	}
	public void setDsAwb(String dsAwb) {
		this.dsAwb = dsAwb;
	}
	public String getDsFormatedAwb() {
		return dsFormatedAwb;
	}
	public void setDsFormatedAwb(String dsFormatedAwb) {
		this.dsFormatedAwb = dsFormatedAwb;
	}
	public DateTime getDhEmissao() {
		return dhEmissao;
	}
	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
}
