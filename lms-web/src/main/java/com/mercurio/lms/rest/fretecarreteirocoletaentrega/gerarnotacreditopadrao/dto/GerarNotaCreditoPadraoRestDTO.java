package com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class GerarNotaCreditoPadraoRestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idControleCarga;
	private Long nrControleCarga;
	private String sgFilialControleCarga;

	private Long qtDoctoColetaPendentes;
	private Long nrNotaCreditoGeradas;

	private DomainValue blPendente;

	private String tpIdentificacaoProprietario;
	private String nrIdentificacaoProprietario;
	private String tpIdentificacaoProprietarioFormatado;
	private String nmPessoaProprietario;

	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public Long getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public String getSgFilialControleCarga() {
		return sgFilialControleCarga;
	}

	public void setSgFilialControleCarga(String sgFilialControleCarga) {
		this.sgFilialControleCarga = sgFilialControleCarga;
	}

	public Long getQtDoctoColetaPendentes() {
		return qtDoctoColetaPendentes;
	}

	public void setQtDoctoColetaPendentes(Long qtDoctoColetaPendentes) {
		this.qtDoctoColetaPendentes = qtDoctoColetaPendentes;
	}

	public Long getNrNotaCreditoGeradas() {
		return nrNotaCreditoGeradas;
	}

	public void setNrNotaCreditoGeradas(Long nrNotaCreditoGeradas) {
		this.nrNotaCreditoGeradas = nrNotaCreditoGeradas;
	}

	public DomainValue getBlPendente() {
		return blPendente;
	}

	public void setBlPendente(DomainValue blPendente) {
		this.blPendente = blPendente;
	}

	public String getTpIdentificacaoProprietario() {
		return tpIdentificacaoProprietario;
	}

	public void setTpIdentificacaoProprietario(
			String tpIdentificacaoProprietario) {
		this.tpIdentificacaoProprietario = tpIdentificacaoProprietario;
	}

	public String getNrIdentificacaoProprietario() {
		return nrIdentificacaoProprietario;
	}

	public void setNrIdentificacaoProprietario(
			String nrIdentificacaoProprietario) {
		this.nrIdentificacaoProprietario = nrIdentificacaoProprietario;
	}

	public String getTpIdentificacaoProprietarioFormatado() {
		return tpIdentificacaoProprietarioFormatado;
	}

	public void setTpIdentificacaoProprietarioFormatado(
			String tpIdentificacaoProprietarioFormatado) {
		this.tpIdentificacaoProprietarioFormatado = tpIdentificacaoProprietarioFormatado;
	}

	public String getNmPessoaProprietario() {
		return nmPessoaProprietario;
	}

	public void setNmPessoaProprietario(String nmPessoaProprietario) {
		this.nmPessoaProprietario = nmPessoaProprietario;
	}

}