package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;

public class CptComplexidade implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCptComplexidade;
	
	private DomainValue tpComplexidade;
	
	private BigDecimal  vlComplexidade;
	
	private DomainValue tpMedidaComplexidade;
	
	private DomainValue tpSituacao;
	
	private CptTipoValor cptTipoValor;

	public Long getIdCptComplexidade() {
		return idCptComplexidade;
	}

	public void setIdCptComplexidade(Long idCptComplexidade) {
		this.idCptComplexidade = idCptComplexidade;
	}

	public DomainValue getTpComplexidade() {
		return tpComplexidade;
	}

	public void setTpComplexidade(DomainValue tpComplexidade) {
		this.tpComplexidade = tpComplexidade;
	}

	public BigDecimal getVlComplexidade() {
		return vlComplexidade;
	}

	public void setVlComplexidade(BigDecimal vlComplexidade) {
		this.vlComplexidade = vlComplexidade;
	}

	public DomainValue getTpMedidaComplexidade() {
		return tpMedidaComplexidade;
	}

	public void setTpMedidaComplexidade(DomainValue tpMedidaComplexidade) {
		this.tpMedidaComplexidade = tpMedidaComplexidade;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public CptTipoValor getCptTipoValor() {
		return cptTipoValor;
	}

	public void setCptTipoValor(CptTipoValor cptTipoValor) {
		this.cptTipoValor = cptTipoValor;
	} 
	
}
