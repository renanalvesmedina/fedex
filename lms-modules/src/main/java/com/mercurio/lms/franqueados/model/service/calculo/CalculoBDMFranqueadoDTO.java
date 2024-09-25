package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;

public class CalculoBDMFranqueadoDTO extends DocumentoFranqueadoDTO {

	private static final long serialVersionUID = 1L;

	private BigDecimal vlParticipacao;
	private String sgFilial;
	private String nrBDM;
	private DomainValue tpDocumentoServico;
	private String sgFilialOrigem;
	private String nrDoctoServico;
	
	@Override
	public CalculoBDMFranqueados createCalculo() {
		return new CalculoBDMFranqueados();
	}

	public BigDecimal getVlParticipacao() {
		return vlParticipacao;
	}

	public void setVlParticipacao(BigDecimal vlParticipacao) {
		this.vlParticipacao = vlParticipacao;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public DomainValue getTpDocumentoServico() {
		return tpDocumentoServico;
	}

	public void setTpDocumentoServico(DomainValue tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public String getSgFilialOrigem() {
		return sgFilialOrigem;
	}

	public void setSgFilialOrigem(String sgFilialOrigem) {
		this.sgFilialOrigem = sgFilialOrigem;
	}

	public String getNrDoctoServico() {
		return nrDoctoServico;
	}

	public void setNrDoctoServico(String nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}

	public void setNrBDM(String nrBDM) {
		this.nrBDM = nrBDM;
	}

	public String getNrBDM() {
		return nrBDM;
	}
}
