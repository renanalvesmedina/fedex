package com.mercurio.lms.entrega.model;

import java.io.Serializable;

public class Veiculos implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long propPessId;
	private String codPlaca;
	private Long nroPlaca;
	
	public Long getPropPessId() {
		return propPessId;
	}

	public void setPropPessId(Long propPessId) {
		this.propPessId = propPessId;
	}

	public String getCodPlaca() {
		return codPlaca;
	}

	public void setCodPlaca(String codPlaca) {
		this.codPlaca = codPlaca;
	}

	public Long getNroPlaca() {
		return nroPlaca;
	}

	public void setNroPlaca(Long nroPlaca) {
		this.nroPlaca = nroPlaca;
	}
			
}
