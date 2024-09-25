package com.mercurio.lms.rest.vendas.dto;

import java.io.Serializable;


public class ClienteSuggestFilterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String value;
	private String modal;
	private String abrangencia;
	private String tpCliente;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getModal() {
		return modal;
	}
	public void setModal(String modal) {
		this.modal = modal;
	}
	public String getAbrangencia() {
		return abrangencia;
	}
	public void setAbrangencia(String abrangencia) {
		this.abrangencia = abrangencia;
	}
	public String getTpCliente() {
		return tpCliente;
	}
	public void setTpCliente(String tpCliente) {
		this.tpCliente = tpCliente;
	}
}
