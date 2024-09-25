package com.mercurio.lms.sgr.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

public class FiltroLiberacaoMotoristaDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private DateTime dhPeriodoIni;
	private DateTime dhPeriodoFim;
	private String cpfMotorista;
	private String nmMotorista;
	
	public DateTime getDhPeriodoIni() {
		return dhPeriodoIni;
	}
	public void setDhPeriodoIni(DateTime dhPeriodoIni) {
		this.dhPeriodoIni = dhPeriodoIni;
	}
	public DateTime getDhPeriodoFim() {
		return dhPeriodoFim;
	}
	public void setDhPeriodoFim(DateTime dhPeriodoFim) {
		this.dhPeriodoFim = dhPeriodoFim;
	}
	public String getCpfMotorista() {
		return cpfMotorista;
	}
	public void setCpfMotorista(String cpfMotorista) {
		this.cpfMotorista = cpfMotorista;
	}
	public String getNmMotorista() {
		return nmMotorista;
	}
	public void setNmMotorista(String nmMotorista) {
		this.nmMotorista = nmMotorista;
	}
	
}
