package com.mercurio.lms.rest.vol.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecuteControleEquipamentoResponseDTO {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("sucesso")
	private boolean sucesso;
	
	@JsonProperty("mensagem")
	private String mensagem;

	public ExecuteControleEquipamentoResponseDTO() {
	}

	public ExecuteControleEquipamentoResponseDTO(boolean sucesso,
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
