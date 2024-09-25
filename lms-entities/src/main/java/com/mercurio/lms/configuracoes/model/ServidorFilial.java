package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import com.mercurio.lms.municipios.model.Filial;

public class ServidorFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServidorFilial;

    /** persistent field */
    private Filial filial;

    /** persistent field */
    private Long nrIpInicial;

    /** persistent field */
    private Long nrIpFinal;

    /** persistent field */
    private Long nrIpServidor;

	/**
	 * @return the idServidorFilial
	 */
	public Long getIdServidorFilial() {
		return idServidorFilial;
	}

	/**
	 * @param idServidorFilial
	 *            the idServidorFilial to set
	 */
	public void setIdServidorFilial(Long idServidorFilial) {
		this.idServidorFilial = idServidorFilial;
	}

	/**
	 * @return the filial
	 */
	public Filial getFilial() {
		return filial;
	}

	/**
	 * @param filial
	 *            the filial to set
	 */
	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	/**
	 * @return the nrIpInicial
	 */
	public Long getNrIpInicial() {
		return nrIpInicial;
	}

	/**
	 * @param nrIpInicial
	 *            the nrIpInicial to set
	 */
	public void setNrIpInicial(Long nrIpInicial) {
		this.nrIpInicial = nrIpInicial;
	}

	/**
	 * @return the nrIpFinal
	 */
	public Long getNrIpFinal() {
		return nrIpFinal;
	}

	/**
	 * @param nrIpFinal
	 *            the nrIpFinal to set
	 */
	public void setNrIpFinal(Long nrIpFinal) {
		this.nrIpFinal = nrIpFinal;
	}

	/**
	 * @return the nrIpServidor
	 */
	public Long getNrIpServidor() {
		return nrIpServidor;
	}

	/**
	 * @param nrIpServidor
	 *            the nrIpServidor to set
	 */
	public void setNrIpServidor(Long nrIpServidor) {
		this.nrIpServidor = nrIpServidor;
	}
}
