package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class SolicitacaoEscoltaTableDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private DateTime dhInclusao;
	private DateTime dhInicioPrevisto;
	private String dsExigenciaGerRisco;
	private FilialSuggestDTO filialSolicitante;
	private Long nrSolicitacaoEscolta;
	private String dsOrigem;
	private String dsDestino;
	private DomainValue tpSituacao;
	private ClienteSuggestDTO clienteEscolta;
	private Long qtViaturas;

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhInicioPrevisto() {
		return dhInicioPrevisto;
	}

	public void setDhInicioPrevisto(DateTime dhInicioPrevisto) {
		this.dhInicioPrevisto = dhInicioPrevisto;
	}

	public String getDsExigenciaGerRisco() {
		return dsExigenciaGerRisco;
	}

	public void setDsExigenciaGerRisco(String dsExigenciaGerRisco) {
		this.dsExigenciaGerRisco = dsExigenciaGerRisco;
	}

	public FilialSuggestDTO getFilialSolicitante() {
		return filialSolicitante;
	}

	public void setFilialSolicitante(FilialSuggestDTO filialSolicitante) {
		this.filialSolicitante = filialSolicitante;
	}

	public Long getNrSolicitacaoEscolta() {
		return nrSolicitacaoEscolta;
	}

	public void setNrSolicitacaoEscolta(Long nrSolicitacaoEscolta) {
		this.nrSolicitacaoEscolta = nrSolicitacaoEscolta;
	}

	public String getDsOrigem() {
		return dsOrigem;
	}

	public void setDsOrigem(String dsOrigem) {
		this.dsOrigem = dsOrigem;
	}

	public String getDsDestino() {
		return dsDestino;
	}

	public void setDsDestino(String dsDestino) {
		this.dsDestino = dsDestino;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public ClienteSuggestDTO getClienteEscolta() {
		return clienteEscolta;
	}

	public void setClienteEscolta(ClienteSuggestDTO clienteEscolta) {
		this.clienteEscolta = clienteEscolta;
	}

	public Long getQtViaturas() {
		return qtViaturas;
	}

	public void setQtViaturas(Long qtViaturas) {
		this.qtViaturas = qtViaturas;
	}

}
