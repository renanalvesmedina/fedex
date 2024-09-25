package com.mercurio.lms.carregamento.model;


import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ComprovanteEntrega implements Serializable {
	
	private static final long serialVersionUID = 6466861968225280842L;

	private Long idComprovanteEntrega;
	
	private Long idDoctoServico;
	
	private Blob assinatura;

	public Long getIdComprovanteEntrega() {
		return idComprovanteEntrega;
	}

	public void setIdComprovanteEntrega(Long idComprovanteEntrega) {
		this.idComprovanteEntrega = idComprovanteEntrega;
	}

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public Blob getAssinatura() {
		return assinatura;
	}

	public void setAssinatura(Blob assinatura) {
		this.assinatura = assinatura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idComprovanteEntrega == null) ? 0 : idComprovanteEntrega.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ComprovanteEntrega other = (ComprovanteEntrega) obj;
		if (idComprovanteEntrega == null) {
			if (other.idComprovanteEntrega != null) {
				return false;
			}
		} else if (!idComprovanteEntrega.equals(other.idComprovanteEntrega)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
