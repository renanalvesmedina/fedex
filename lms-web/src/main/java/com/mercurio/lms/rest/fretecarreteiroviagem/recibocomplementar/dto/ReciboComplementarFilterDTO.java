package com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.dto;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.dto.ReciboFreteCarreteiroSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class ReciboComplementarFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private Long idReciboFreteCarreteiro;
	private Long nrReciboFreteCarreteiro;

	private Long nrRelacaoPagamento;

	private DomainValue tpSituacaoRecibo;
	private DomainValue tpReciboFreteCarreteiro;

	private DateTime dhEmissaoInicial;
	private DateTime dhEmissaoFinal;

	private YearMonthDay dtPagtoRealInicial;
	private YearMonthDay dtPagtoRealFinal;

	private YearMonthDay dtProgramadaPagtoInicial;
	private YearMonthDay dtProgramadaPagtoFinal;

	private FilialSuggestDTO filial;
	private ProprietarioDTO proprietario;
	private MeioTransporteSuggestDTO meioTransporte;
	private ControleCargaSuggestDTO controleCarga;
	private ReciboFreteCarreteiroSuggestDTO reciboComplementado;

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

	public Long getNrRelacaoPagamento() {
		return nrRelacaoPagamento;
	}

	public void setNrRelacaoPagamento(Long nrRelacaoPagamento) {
		this.nrRelacaoPagamento = nrRelacaoPagamento;
	}

	public DomainValue getTpSituacaoRecibo() {
		return tpSituacaoRecibo;
	}

	public void setTpSituacaoRecibo(DomainValue tpSituacaoRecibo) {
		this.tpSituacaoRecibo = tpSituacaoRecibo;
	}

	public DomainValue getTpReciboFreteCarreteiro() {
		return tpReciboFreteCarreteiro;
	}

	public void setTpReciboFreteCarreteiro(DomainValue tpReciboFreteCarreteiro) {
		this.tpReciboFreteCarreteiro = tpReciboFreteCarreteiro;
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

	public YearMonthDay getDtPagtoRealInicial() {
		return dtPagtoRealInicial;
	}

	public void setDtPagtoRealInicial(YearMonthDay dtPagtoRealInicial) {
		this.dtPagtoRealInicial = dtPagtoRealInicial;
	}

	public YearMonthDay getDtPagtoRealFinal() {
		return dtPagtoRealFinal;
	}

	public void setDtPagtoRealFinal(YearMonthDay dtPagtoRealFinal) {
		this.dtPagtoRealFinal = dtPagtoRealFinal;
	}

	public YearMonthDay getDtProgramadaPagtoInicial() {
		return dtProgramadaPagtoInicial;
	}

	public void setDtProgramadaPagtoInicial(
			YearMonthDay dtProgramadaPagtoInicial) {
		this.dtProgramadaPagtoInicial = dtProgramadaPagtoInicial;
	}

	public YearMonthDay getDtProgramadaPagtoFinal() {
		return dtProgramadaPagtoFinal;
	}

	public void setDtProgramadaPagtoFinal(YearMonthDay dtProgramadaPagtoFinal) {
		this.dtProgramadaPagtoFinal = dtProgramadaPagtoFinal;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
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

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public ReciboFreteCarreteiroSuggestDTO getReciboComplementado() {
		return reciboComplementado;
	}

	public void setReciboComplementado(
			ReciboFreteCarreteiroSuggestDTO reciboComplementado) {
		this.reciboComplementado = reciboComplementado;
	}

}