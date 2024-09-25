package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class ClienteTerritorioDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private ClienteSuggestDTO cliente;
	private TerritorioSuggestDTO territorio;
	private DomainValue tpModal;
	private YearMonthDay dtInicio;
	private YearMonthDay dtFim;
	private FilialSuggestDTO filialResponsavel;
	private FilialSuggestDTO filialNegociacao;
	private DomainValue tpCliente;
	private DomainValue tpSituacao;
	private ClienteTerritorioTransferenciaCarteiraDTO clienteTerritorioTransferenciaCarteiraDTO;
	private Long idPendeciaAprovacao;

	private Boolean blComissaoConquista = false; 
	private Long idComissaoConquista = null;
	private YearMonthDay dtComissaoConquistaInicio;
	private YearMonthDay dtComissaoConquistaFim;
	
	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public TerritorioSuggestDTO getTerritorio() {
		return territorio;
	}

	public void setTerritorio(TerritorioSuggestDTO territorio) {
		this.territorio = territorio;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
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

	public FilialSuggestDTO getFilialResponsavel() {
		return filialResponsavel;
	}

	public void setFilialResponsavel(FilialSuggestDTO filialResponsavel) {
		this.filialResponsavel = filialResponsavel;
	}

	public FilialSuggestDTO getFilialNegociacao() {
		return filialNegociacao;
	}

	public void setFilialNegociacao(FilialSuggestDTO filialNegociacao) {
		this.filialNegociacao = filialNegociacao;
	}

	public DomainValue getTpCliente() {
		return tpCliente;
	}

	public void setTpCliente(DomainValue tpCliente) {
		this.tpCliente = tpCliente;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public void setClienteTerritorioTransferenciaCarteiraDTO(
			ClienteTerritorioTransferenciaCarteiraDTO clienteTerritorioTransferenciaCarteiraDTO) {
		this.clienteTerritorioTransferenciaCarteiraDTO = clienteTerritorioTransferenciaCarteiraDTO;
	}
	
	public ClienteTerritorioTransferenciaCarteiraDTO getClienteTerritorioTransferenciaCarteiraDTO() {
		return clienteTerritorioTransferenciaCarteiraDTO;
	}
	
	public Long getIdPendeciaAprovacao() {
		return idPendeciaAprovacao;
	}
	
	public void setIdPendeciaAprovacao(Long idPendeciaAprovacao) {
		this.idPendeciaAprovacao = idPendeciaAprovacao;
	}

	public Boolean getBlComissaoConquista() {
		return blComissaoConquista;
	}

	public void setBlComissaoConquista(Boolean blComissaoConquista) {
		this.blComissaoConquista = blComissaoConquista;
	}

	public Long getIdComissaoConquista() {
		return idComissaoConquista;
	}

	public void setIdComissaoConquista(Long idComissaoConquista) {
		this.idComissaoConquista = idComissaoConquista;
	}

	public YearMonthDay getDtComissaoConquistaInicio() {
		return dtComissaoConquistaInicio;
	}

	public void setDtComissaoConquistaInicio(YearMonthDay dtComissaoConquistaInicio) {
		this.dtComissaoConquistaInicio = dtComissaoConquistaInicio;
	}

	public YearMonthDay getDtComissaoConquistaFim() {
		return dtComissaoConquistaFim;
	}

	public void setDtComissaoConquistaFim(YearMonthDay dtComissaoConquistaFim) {
		this.dtComissaoConquistaFim = dtComissaoConquistaFim;
	}
	
}
