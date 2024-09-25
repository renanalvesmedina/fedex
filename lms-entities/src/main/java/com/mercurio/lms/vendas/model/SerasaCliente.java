package com.mercurio.lms.vendas.model;

import java.io.Serializable;

/** @author LMS Custom Hibernate CodeGenerator */
public class SerasaCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idSerasaCliente;

	/** identifier field */
	private byte[] imPdfSerasa;

	/** persistent field */
	private HistoricoAnaliseCredito historicoAnaliseCredito;

	public Long getIdSerasaCliente() {
		return idSerasaCliente;
	}

	public void setIdSerasaCliente(Long idSerasaCliente) {
		this.idSerasaCliente = idSerasaCliente;
	}

	public byte[] getImPdfSerasa() {
		return imPdfSerasa;
	}

	public void setImPdfSerasa(byte[] imPdfSerasa) {
		this.imPdfSerasa = imPdfSerasa;
	}

	public HistoricoAnaliseCredito getHistoricoAnaliseCredito() {
		return historicoAnaliseCredito;
	}

	public void setHistoricoAnaliseCredito(
			HistoricoAnaliseCredito historicoAnaliseCredito) {
		this.historicoAnaliseCredito = historicoAnaliseCredito;
	}
}