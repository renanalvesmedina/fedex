package com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.dto.ReciboFreteCarreteiroSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class NotaCreditoPadraoFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private Long idNotaCredito;
	private Long nrNotaCredito;

	private Long idControleCarga;
	
	private FilialSuggestDTO filial;
	private ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiro;
	private ProprietarioDTO proprietario;
	private MeioTransporteSuggestDTO meioTransporte;

	private DateTime dhEmissaoInicial;
	private DateTime dhEmissaoFinal;

	private DateTime dhGeracaoInicial;
	private DateTime dhGeracaoFinal;

	private DomainValue tpSituacao;
	private DomainValue tpNotaCredito;
	private DomainValue tpMostrarNotaZerada;

	public Long getIdNotaCredito() {
		return idNotaCredito;
	}

	public void setIdNotaCredito(Long idNotaCredito) {
		this.idNotaCredito = idNotaCredito;
	}

	public Long getNrNotaCredito() {
		return nrNotaCredito;
	}

	public void setNrNotaCredito(Long nrNotaCredito) {
		this.nrNotaCredito = nrNotaCredito;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public ReciboFreteCarreteiroSuggestDTO getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(
			ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}

	public MeioTransporteSuggestDTO getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporteSuggestDTO meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public DateTime getDhEmissaoInicial() {
		return dhEmissaoInicial;
	}

	public void setDhEmissaoInicial(DateTime dhEmissaoInicial) {
		this.dhEmissaoInicial = dhEmissaoInicial;
	}

	public DateTime getDhEmissaoFinal() {
		return dhEmissaoFinal;
	}

	public void setDhEmissaoFinal(DateTime dhEmissaoFinal) {
		this.dhEmissaoFinal = dhEmissaoFinal;
	}

	public DateTime getDhGeracaoInicial() {
		return dhGeracaoInicial;
	}

	public void setDhGeracaoInicial(DateTime dhGeracaoInicial) {
		this.dhGeracaoInicial = dhGeracaoInicial;
	}

	public DateTime getDhGeracaoFinal() {
		return dhGeracaoFinal;
	}

	public void setDhGeracaoFinal(DateTime dhGeracaoFinal) {
		this.dhGeracaoFinal = dhGeracaoFinal;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpNotaCredito() {
		return tpNotaCredito;
	}

	public void setTpNotaCredito(DomainValue tpNotaCredito) {
		this.tpNotaCredito = tpNotaCredito;
	}

	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public DomainValue getTpMostrarNotaZerada() {
		return tpMostrarNotaZerada;
	}

	public void setTpMostrarNotaZerada(DomainValue tpMostrarNotaZerada) {
		this.tpMostrarNotaZerada = tpMostrarNotaZerada;
	}

}