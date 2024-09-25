package com.mercurio.lms.rest.expedicao.dto;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;
import com.mercurio.adsm.framework.model.DomainValue;

public class LocalizacaoAwbCiaAereaFilterDTO extends BaseFilterDTO {
	
	private EmpresaDTO empresa;
	private String dsTracking;
	private DomainValue tpLocalizacaoCiaAerea;
	private DomainValue tpLocalizacaoAtual;
	
	public EmpresaDTO getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}
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
}
