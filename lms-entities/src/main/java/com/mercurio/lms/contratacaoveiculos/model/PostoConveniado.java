package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import com.mercurio.lms.configuracoes.model.Pessoa;

public class PostoConveniado implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idPostoConveniado;
	private Pessoa pessoa;
	
	public Long getIdPostoConveniado() {
		return idPostoConveniado;
	}

	public void setIdPostoConveniado(Long idPostoConveniado) {
		this.idPostoConveniado = idPostoConveniado;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
