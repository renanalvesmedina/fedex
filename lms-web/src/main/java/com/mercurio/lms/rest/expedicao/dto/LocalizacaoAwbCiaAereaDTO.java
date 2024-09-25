package com.mercurio.lms.rest.expedicao.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;

public class LocalizacaoAwbCiaAereaDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	
	private EmpresaDTO ciaAerea; 
	private String dsTracking;
	private DomainValue tpLocalizacaoCiaAerea;
	private DomainValue tpLocalizacaoAtual;
	
	public String getDsTracking() {
		return dsTracking;
	}
	public void setDsTracking(String dsTracking) {
		this.dsTracking = dsTracking;
	}
	public DomainValue getTpLocalizacaoCiaAerea() {
		return tpLocalizacaoCiaAerea;
	}
	public void setTpLocalizacaoCiaAerea(DomainValue tpLocalizacaoCiaAerea) {
		this.tpLocalizacaoCiaAerea = tpLocalizacaoCiaAerea;
	}
	public DomainValue getTpLocalizacaoAtual() {
		return tpLocalizacaoAtual;
	}
	public void setTpLocalizacaoAtual(DomainValue tpLocalizacaoAtual) {
		this.tpLocalizacaoAtual = tpLocalizacaoAtual;
	}
	public EmpresaDTO getCiaAerea() {
		return ciaAerea;
	}
	public void setCiaAerea(EmpresaDTO ciaAerea) {
		this.ciaAerea = ciaAerea;
	}
}
