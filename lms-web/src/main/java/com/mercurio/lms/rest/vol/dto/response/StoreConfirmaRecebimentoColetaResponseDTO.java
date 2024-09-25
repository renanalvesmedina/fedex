package com.mercurio.lms.rest.vol.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreConfirmaRecebimentoColetaResponseDTO {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("sucesso")
	private boolean sucesso;
	
	@JsonProperty("mensagem")
	private String mensagem;

	public StoreConfirmaRecebimentoColetaResponseDTO() {
	}

	public StoreConfirmaRecebimentoColetaResponseDTO(boolean sucesso,
			String mensagem) {
		super();
		this.sucesso = sucesso;
		this.mensagem = mensagem;
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
	
}
