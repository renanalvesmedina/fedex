package com.mercurio.lms.contasreceber.model.param;

import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;


public class ArquivoRecebidoPreFaturaPadraoTelaParam extends ArquivoRecebidoPreFaturaTelaParam{
		
	private Integer qtDocumentos;
	
	private String fileName;
	
	private String nrPreFatura;
	
	private List<Long> lstDevedorDocServFat;

	private DomainValue tpModal;

	private DomainValue tpAbrangencia;

	public Integer getQtDocumentos() {
		return qtDocumentos;
	}

	public void setQtDocumentos(Integer qtDocumentos) {
		this.qtDocumentos = qtDocumentos;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Long> getLstDevedorDocServFat() {
		return lstDevedorDocServFat;
	}

	public void setLstDevedorDocServFat(List<Long> lstDevedorDocServFat) {
		this.lstDevedorDocServFat = lstDevedorDocServFat;
	}	

	public String getNrPreFatura() {
		return nrPreFatura;
}

	public void setNrPreFatura(String nrPreFatura) {
		this.nrPreFatura = nrPreFatura;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public void setTpAbrancencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}
	
	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}
	
	public DomainValue getTpModal() {
		return tpModal;
	}
}
