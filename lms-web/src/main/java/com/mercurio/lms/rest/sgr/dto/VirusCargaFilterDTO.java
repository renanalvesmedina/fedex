package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class VirusCargaFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private ControleCargaSuggestDTO controleCarga;
	private DateTime dhAtivacaoInicial;
	private DateTime dhAtivacaoFinal;
	private DateTime dhInclusaoInicial;
	private DateTime dhInclusaoFinal;
	private ClienteSuggestDTO cliente;
	private String nrChave;
	private String dsSerie;
	private String nrIscaCarga;
	private Long nrNotaFiscal;
	private String nrVolume;

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public DateTime getDhAtivacaoInicial() {
		return dhAtivacaoInicial;
	}

	public void setDhAtivacaoInicial(DateTime dhAtivacaoInicial) {
		this.dhAtivacaoInicial = dhAtivacaoInicial;
	}

	public DateTime getDhAtivacaoFinal() {
		return dhAtivacaoFinal;
	}

	public void setDhAtivacaoFinal(DateTime dhAtivacaoFinal) {
		this.dhAtivacaoFinal = dhAtivacaoFinal;
	}

	public DateTime getDhInclusaoInicial() {
		return dhInclusaoInicial;
	}

	public void setDhInclusaoInicial(DateTime dhInclusaoInicial) {
		this.dhInclusaoInicial = dhInclusaoInicial;
	}

	public DateTime getDhInclusaoFinal() {
		return dhInclusaoFinal;
	}

	public void setDhInclusaoFinal(DateTime dhInclusaoFinal) {
		this.dhInclusaoFinal = dhInclusaoFinal;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public String getDsSerie() {
		return dsSerie;
	}

	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}

	public String getNrIscaCarga() {
		return nrIscaCarga;
	}

	public void setNrIscaCarga(String nrIscaCarga) {
		this.nrIscaCarga = nrIscaCarga;
	}

	public Long getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Long nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public String getNrVolume() {
		return nrVolume;
	}

	public void setNrVolume(String nrVolume) {
		this.nrVolume = nrVolume;
	}

}
