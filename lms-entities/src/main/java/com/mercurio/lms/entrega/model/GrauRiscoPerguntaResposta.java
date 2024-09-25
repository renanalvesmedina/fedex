package com.mercurio.lms.entrega.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class GrauRiscoPerguntaResposta implements Serializable {
	private static final long serialVersionUID = -1087670038898252359L;
	private Long idGrauRiscoPerguntaResposta;
	private Long idGrauRiscoPergunta;
	private Long idComprovanteEntrega;
	private Boolean blGrauRiscoPerguntaResposta;
	
	public Long getIdGrauRiscoPerguntaResposta() {
		return idGrauRiscoPerguntaResposta;
	}
	public void setIdGrauRiscoPerguntaResposta(Long idGrauRiscoPerguntaResposta) {
		this.idGrauRiscoPerguntaResposta = idGrauRiscoPerguntaResposta;
	}
	public Long getIdGrauRiscoPergunta() {
		return idGrauRiscoPergunta;
	}
	public void setIdGrauRiscoPergunta(Long idGrauRiscoPergunta) {
		this.idGrauRiscoPergunta = idGrauRiscoPergunta;
	}
	public Long getIdComprovanteEntrega() {
		return idComprovanteEntrega;
	}
	public void setIdComprovanteEntrega(Long idComprovanteEntrega) {
		this.idComprovanteEntrega = idComprovanteEntrega;
	}
	public Boolean getBlGrauRiscoPerguntaResposta() {
		return blGrauRiscoPerguntaResposta;
	}
	public void setBlGrauRiscoPerguntaResposta(Boolean blGrauRiscoPerguntaResposta) {
		this.blGrauRiscoPerguntaResposta = blGrauRiscoPerguntaResposta;
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
		GrauRiscoPerguntaResposta other = (GrauRiscoPerguntaResposta) obj;
		if (idGrauRiscoPerguntaResposta == null) {
			if (other.idGrauRiscoPerguntaResposta != null) {
				return false;
			}
		} else if (!idGrauRiscoPerguntaResposta.equals(other.idGrauRiscoPerguntaResposta)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idGrauRiscoPerguntaResposta == null) ? 0 : idGrauRiscoPerguntaResposta.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
	
}
