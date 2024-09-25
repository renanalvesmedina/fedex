package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class VirusCargaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private DateTime dhAtivacao;
	private DateTime dhInclusao;
	private ClienteSuggestDTO cliente;
	private String nrChave;
	private String dsSerie;
	private String nrIscaCarga;
	private Long nrNotaFiscal;
	private String nrVolume;

	public DateTime getDhAtivacao() {
		return dhAtivacao;
	}

	public void setDhAtivacao(DateTime dhAtivacao) {
		this.dhAtivacao = dhAtivacao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
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
