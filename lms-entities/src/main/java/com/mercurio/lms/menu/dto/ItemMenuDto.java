package com.mercurio.lms.menu.dto;

import java.io.Serializable;


public class ItemMenuDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String texto;
	private String acao;
	private transient Long level;

	public ItemMenuDto() {}
	
	public ItemMenuDto(String texto, String acao, Long level) {
		super();
		this.texto = texto;
		this.acao = acao;
		this.level = level;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	public Long getLevel() {
		return level;
	}
	
	public void setLevel(Long level) {
		this.level = level;
	}

}
