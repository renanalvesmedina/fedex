package com.mercurio.lms.portaria.model;

import java.io.Serializable;

public class MotoristaTerceiro  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idMotoristaTerceiro;
	
	private String nmMotorista;
	
	private String nrRg;
	
	private Long nrCpf;
	
	private Long nrCnh;
	
	/**
	 * @return Returns the idMotoristaTerceiro.
	 */
	public Long getIdMotoristaTerceiro() {
		return idMotoristaTerceiro;
	}

	/**
	 * @param idMotoristaTerceiro
	 *            The idMotoristaTerceiro to set.
	 */
	public void setIdMotoristaTerceiro(Long idMotoristaTerceiro) {
		this.idMotoristaTerceiro = idMotoristaTerceiro;
	}

	/**
	 * @return Returns the nmMotorista.
	 */
	public String getNmMotorista() {
		return nmMotorista;
	}

	/**
	 * @param nmMotorista
	 *            The nmMotorista to set.
	 */
	public void setNmMotorista(String nmMotorista) {
		this.nmMotorista = nmMotorista;
	}

	/**
	 * @return Returns the nrCnh.
	 */
	public Long getNrCnh() {
		return nrCnh;
	}

	/**
	 * @param nrCnh
	 *            The nrCnh to set.
	 */
	public void setNrCnh(Long nrCnh) {
		this.nrCnh = nrCnh;
	}

	/**
	 * @return Returns the nrCpf.
	 */
	public Long getNrCpf() {
		return nrCpf;
	}

	/**
	 * @param nrCpf
	 *            The nrCpf to set.
	 */
	public void setNrCpf(Long nrCpf) {
		this.nrCpf = nrCpf;
	}

	/**
	 * @return Returns the nrRg.
	 */
	public String getNrRg() {
		return nrRg;
	}

	/**
	 * @param nrRg
	 *            The nrRg to set.
	 */
	public void setNrRg(String nrRg) {
		this.nrRg = nrRg;
	}
	
}
