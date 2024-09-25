package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.List;

public class LogDetalhe implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8575325188718195710L;
	private String status;
	private String observacao;
	private long idLogMestre;
	private List<Registro> registros;
	
	
	public List<Registro> getRegistros() {
		return registros;
	}
	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public long getIdLogMestre() {
		return idLogMestre;
	}
	public void setIdLogMestre(long idLogMestre) {
		this.idLogMestre = idLogMestre;
	}

	
	
}
