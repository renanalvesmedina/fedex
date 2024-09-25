package com.mercurio.lms.entrega.model;


import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ComprovanteEntrega implements Serializable {
	
	private static final long serialVersionUID = 6466861968225280842L;

	private Long idComprovanteEntrega;
	
	private Long idDoctoServico;
	
	private Blob assinatura;
	
	private Boolean blEnviado;
	
	private Long idNotaFiscalConhecimento;
	
	private Long idUsuarioInclusao;
	
	private Long nrTentativaEnvio;
	
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

	public Boolean getBlEnviado() {
		return blEnviado;
	}

	public void setBlEnviado(Boolean blEnviado) {
		this.blEnviado = blEnviado;
	}

	public Long getIdNotaFiscalConhecimento() {
		return idNotaFiscalConhecimento;
	}

	public void setIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		this.idNotaFiscalConhecimento = idNotaFiscalConhecimento;
	}

	public Long getIdUsuarioInclusao() {
		return idUsuarioInclusao;
	}

	public void setIdUsuarioInclusao(Long idUsuarioInclusao) {
		this.idUsuarioInclusao = idUsuarioInclusao;
	}   

	public Long getNrTentativaEnvio() {
		return nrTentativaEnvio;
	}

	public void setNrTentativaEnvio(Long nrTentativaEnvio) {
		this.nrTentativaEnvio = nrTentativaEnvio;
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
