package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;

/** @author LMS Custom Hibernate CodeGenerator */
public class DoctoServicoFin implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idDoctoServico;

	/** identifier field */
	private Long nrDoctoServico;

	/** persistent field */
	private DomainValue tpDocumentoServico;

	/** persistent field */
	private DomainValue tpFrete;

	/** persistent field */
	private DomainValue tpSituacao;	
	
	/** persistent field */
	private BigDecimal vlTotalDocServico;	
	
	/** persistent field */
	private BigDecimal psReal;

	/** nullable persistent field */
	private BigDecimal psAforado;
	
	/** persistent field */
	private Moeda moeda;

	/** persistent field */
	private Servico servico;

	/** persistent field */
	private Filial filialByIdFilialDestino;

	/** persistent field */
	private Filial filialByIdFilialOrigem;
	
	/** persistent field */
	private Pais paisOrigem;

	public Filial getFilialByIdFilialDestino() {
		return filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public Filial getFilialByIdFilialOrigem() {
		return filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Long getNrDoctoServico() {
		return nrDoctoServico;
	}

	public void setNrDoctoServico(Long nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}

	public BigDecimal getPsAforado() {
		return psAforado;
	}

	public void setPsAforado(BigDecimal psAforado) {
		this.psAforado = psAforado;
	}

	public BigDecimal getPsReal() {
		return psReal;
	}

	public void setPsReal(BigDecimal psReal) {
		this.psReal = psReal;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public DomainValue getTpDocumentoServico() {
		return tpDocumentoServico;
	}

	public void setTpDocumentoServico(DomainValue tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public DomainValue getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public BigDecimal getVlTotalDocServico() {
		return vlTotalDocServico;
	}

	public void setVlTotalDocServico(BigDecimal vlTotalDocServico) {
		this.vlTotalDocServico = vlTotalDocServico;
	}

	public Pais getPaisOrigem() {
		return this.paisOrigem;
	}
	
	public void setPaisOrigem(Pais paisOrigem) {
		this.paisOrigem = paisOrigem;
	}
	
}
