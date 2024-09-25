package com.mercurio.lms.rest.fretecarreteiroviagem.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ReciboFreteCarreteiroSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idReciboFreteCarreteiro;
	private Long nrReciboFreteCarreteiro;

	private String sgFilial;

	private DomainValue tpReciboFreteCarreteiro;
	private DomainValue tpSituacaoRecibo;

	private DateTime dhEmissao;

	public Long getIdReciboFreteCarreteiro() {
		return idReciboFreteCarreteiro;
	}

	public void setIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		this.idReciboFreteCarreteiro = idReciboFreteCarreteiro;
	}

	public Long getNrReciboFreteCarreteiro() {
		return nrReciboFreteCarreteiro;
	}

	public void setNrReciboFreteCarreteiro(Long nrReciboFreteCarreteiro) {
		this.nrReciboFreteCarreteiro = nrReciboFreteCarreteiro;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public DomainValue getTpReciboFreteCarreteiro() {
		return tpReciboFreteCarreteiro;
	}

	public void setTpReciboFreteCarreteiro(DomainValue tpReciboFreteCarreteiro) {
		this.tpReciboFreteCarreteiro = tpReciboFreteCarreteiro;
	}

	public DomainValue getTpSituacaoRecibo() {
		return tpSituacaoRecibo;
	}

	public void setTpSituacaoRecibo(DomainValue tpSituacaoRecibo) {
		this.tpSituacaoRecibo = tpSituacaoRecibo;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
}