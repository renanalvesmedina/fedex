package com.mercurio.lms.vol.model;

import java.io.Serializable;

public class DominioVolDto implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String chave;
	
	private String valor;

	private String descricao;

	public DominioVolDto(String chave, String valor, String descricao) {
		super();
		this.chave = chave;
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
