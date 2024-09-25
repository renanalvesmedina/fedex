package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.Locale;

public class IdiomaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idPortugues;
	private String descricaoPortugues;
	private String descricaoTraduzida;
	private Integer tamanhoColuna;
	private Locale locale;

	public Integer getTamanhoColuna() {
		return tamanhoColuna;
	}

	public void setTamanhoColuna(Integer columnLength) {
		this.tamanhoColuna = columnLength;
	}

	public Integer getIdPortugues() {
		return idPortugues;
	}

	public void setIdPortugues(Integer idIdiomaPortugues) {
		this.idPortugues = idIdiomaPortugues;
	}

	public String getDescricaoTraduzida() {
		return descricaoTraduzida;
	}

	public void setDescricaoTraduzida(String idiomaEstrangeiro) {
		this.descricaoTraduzida = idiomaEstrangeiro;
	}

	public String getDescricaoPortugues() {
		return descricaoPortugues;
	}

	public void setDescricaoPortugues(String idiomaPortugues) {
		this.descricaoPortugues = idiomaPortugues;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
