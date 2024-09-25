package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class DivisaoClienteSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	private Long idDivisaoCliente;
	private String nmDivisaoCliente;

	public DivisaoClienteSuggestDTO() {
	}
	
	public DivisaoClienteSuggestDTO(Long idDivisaoCliente, String nmDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
		this.nmDivisaoCliente = nmDivisaoCliente;
	}

	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}

	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}

	public String getNmDivisaoCliente() {
		return nmDivisaoCliente;
	}

	public void setNmDivisaoCliente(String nmDivisaoCliente) {
		this.nmDivisaoCliente = nmDivisaoCliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idDivisaoCliente == null) ? 0 : idDivisaoCliente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DivisaoClienteSuggestDTO other = (DivisaoClienteSuggestDTO) obj;
		if (idDivisaoCliente == null) {
			if (other.idDivisaoCliente != null)
				return false;
		} else if (!idDivisaoCliente.equals(other.idDivisaoCliente))
			return false;
		return true;
	}
	
	
}
