package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;


public class BotoesPropostaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private boolean btSalvar = false;
	private boolean btImprimirProposta = false;
	private boolean btSolicitarEfetivacao = false;
	private boolean btAprovacao = false;
	private boolean btReprovarEfetivacao = false;
	private boolean btEfetivarProposta = false;
	
	public BotoesPropostaDTO() {
	}
	
	public BotoesPropostaDTO(boolean disableAll) {
		super();
		this.btSalvar = disableAll;
		this.btImprimirProposta = disableAll;
		this.btSolicitarEfetivacao = disableAll;
		this.btAprovacao = disableAll;
		this.btReprovarEfetivacao = disableAll;
		this.btEfetivarProposta = disableAll;
	}

	public boolean isBtSalvar() {
		return btSalvar;
	}

	public void setBtSalvar(boolean btSalvar) {
		this.btSalvar = btSalvar;
	}

	public boolean isBtImprimirProposta() {
		return btImprimirProposta;
	}

	public void setBtImprimirProposta(boolean btImprimirProposta) {
		this.btImprimirProposta = btImprimirProposta;
	}

	public boolean isBtSolicitarEfetivacao() {
		return btSolicitarEfetivacao;
	}

	public void setBtSolicitarEfetivacao(boolean btSolicitarEfetivacao) {
		this.btSolicitarEfetivacao = btSolicitarEfetivacao;
	}

	public boolean isBtAprovacao() {
		return btAprovacao;
	}

	public void setBtAprovacao(boolean btAprovacao) {
		this.btAprovacao = btAprovacao;
	}

	public boolean isBtReprovarEfetivacao() {
		return btReprovarEfetivacao;
	}

	public void setBtReprovarEfetivacao(boolean btReprovarEfetivacao) {
		this.btReprovarEfetivacao = btReprovarEfetivacao;
	}

	public boolean isBtEfetivarProposta() {
		return btEfetivarProposta;
	}

	public void setBtEfetivarProposta(boolean btEfetivarProposta) {
		this.btEfetivarProposta = btEfetivarProposta;
	}

}
