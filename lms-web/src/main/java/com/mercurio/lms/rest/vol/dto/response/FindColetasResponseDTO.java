package com.mercurio.lms.rest.vol.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FindColetasResponseDTO {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("sucesso")
	private boolean sucesso;
	
	@JsonProperty("mensagem")
	private String mensagem;
	
	private Map retorno;
	
	public FindColetasResponseDTO() {
	}

	public FindColetasResponseDTO(boolean sucesso, String mensagem, Map retorno) {
		super();
		this.sucesso = sucesso;
		this.mensagem = mensagem;
		this.retorno = retorno;
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Map getRetorno() {
		return retorno;
	}

	public void setRetorno(Map retorno) {
		this.retorno = retorno;
	}
	
}
