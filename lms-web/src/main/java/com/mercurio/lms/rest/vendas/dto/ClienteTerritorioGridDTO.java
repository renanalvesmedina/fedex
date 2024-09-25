package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.municipios.model.Filial;

public class ClienteTerritorioGridDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String nmPessoa;
	private String nmTerritorio;
	private String tpModal;
	private YearMonthDay dtInicio;
	private YearMonthDay dtFim;
	private String cnpj;
	private String tpCliente;
	private String tpSituacao;
	private String tpSituacaoAprovacao;
	private String sgFilialResponsavel;
	private String sgFilialNegociacao;

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public String getNmTerritorio() {
		return nmTerritorio;
	}

	public void setNmTerritorio(String nmTerritorio) {
		this.nmTerritorio = nmTerritorio;
	}

	public String getTpModal() {
		return tpModal;
	}

	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}

	public YearMonthDay getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}

	public YearMonthDay getDtFim() {
		return dtFim;
	}

	public void setDtFim(YearMonthDay dtFim) {
		this.dtFim = dtFim;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getTpCliente() {
		return tpCliente;
	}

	public void setTpCliente(String tpCliente) {
		this.tpCliente = tpCliente;
	}

	public String getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(String tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public String getSgFilialResponsavel() {
		return sgFilialResponsavel;
	}

	public void setSgFilialResponsavel(String sgFilialResponsavel) {
		this.sgFilialResponsavel = sgFilialResponsavel;
	}

	public String getSgFilialNegociacao() {
		return sgFilialNegociacao;
	}

	public void setSgFilialNegociacao(String sgFilialNegociacao) {
		this.sgFilialNegociacao = sgFilialNegociacao;
	}

}
