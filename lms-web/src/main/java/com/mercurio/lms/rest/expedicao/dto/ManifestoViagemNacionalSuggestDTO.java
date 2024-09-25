package com.mercurio.lms.rest.expedicao.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class ManifestoViagemNacionalSuggestDTO extends BaseDTO{
	private static final long serialVersionUID = 1L;
	
	private Long idManifestoViagem;
	private String sgFilial;
	private DateTime dhEmissao;
	private Integer nrManifestoOrigem;
	
	public Long getIdManifestoViagem() {
		return idManifestoViagem;
	}
	public void setIdManifestoViagem(Long idManifestoViagem) {
		this.idManifestoViagem = idManifestoViagem;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public DateTime getDhEmissao() {
		return dhEmissao;
	}
	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
	public Integer getNrManifestoOrigem() {
		return nrManifestoOrigem;
	}
	public void setNrManifestoOrigem(Integer nrManifestoOrigem) {
		this.nrManifestoOrigem = nrManifestoOrigem;
	}
	
}
