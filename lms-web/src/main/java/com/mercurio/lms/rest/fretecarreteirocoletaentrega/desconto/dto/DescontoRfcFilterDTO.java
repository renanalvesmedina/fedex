package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto;

import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class DescontoRfcFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private FilialSuggestDTO filial;
	private ProprietarioDTO proprietario;
	private MeioTransporteSuggestDTO meioTransporte;

	private Long nrDescontoRfc;

	private DomainValue tpSituacao;
	private DomainValue tpOperacao;

	private YearMonthDay dtAtualizacaoInicial;
	private YearMonthDay dtAtualizacaoFinal;

	private YearMonthDay dtInicioDescontoInicial;
	private YearMonthDay dtInicioDescontoFinal;

	private Map<Object, String> tipoDescontoRfc;

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

	public Long getNrDescontoRfc() {
		return nrDescontoRfc;
	}

	public void setNrDescontoRfc(Long nrDescontoRfc) {
		this.nrDescontoRfc = nrDescontoRfc;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public YearMonthDay getDtAtualizacaoInicial() {
		return dtAtualizacaoInicial;
	}

	public void setDtAtualizacaoInicial(YearMonthDay dtAtualizacaoInicial) {
		this.dtAtualizacaoInicial = dtAtualizacaoInicial;
	}

	public YearMonthDay getDtAtualizacaoFinal() {
		return dtAtualizacaoFinal;
	}

	public void setDtAtualizacaoFinal(YearMonthDay dtAtualizacaoFinal) {
		this.dtAtualizacaoFinal = dtAtualizacaoFinal;
	}

	public Map<Object, String> getTipoDescontoRfc() {
		return tipoDescontoRfc;
	}

	public void setTipoDescontoRfc(Map<Object, String> tipoDescontoRfc) {
		this.tipoDescontoRfc = tipoDescontoRfc;
	}

	public YearMonthDay getDtInicioDescontoInicial() {
		return dtInicioDescontoInicial;
	}

	public void setDtInicioDescontoInicial(YearMonthDay dtInicioDescontoInicial) {
		this.dtInicioDescontoInicial = dtInicioDescontoInicial;
	}

	public YearMonthDay getDtInicioDescontoFinal() {
		return dtInicioDescontoFinal;
	}

	public void setDtInicioDescontoFinal(YearMonthDay dtInicioDescontoFinal) {
		this.dtInicioDescontoFinal = dtInicioDescontoFinal;
	}
}