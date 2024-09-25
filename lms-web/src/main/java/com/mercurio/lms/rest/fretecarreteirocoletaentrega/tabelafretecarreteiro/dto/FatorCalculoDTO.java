package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.rest.BaseDTO;

public class FatorCalculoDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nome;
	private BigDecimal valor;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
