package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class X2009Observacao implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long identObservacao;
	
	private String grupoObservacao;
	
	private String codObservacao;
	
	private YearMonthDay validObservacao; 
	
	private String descricao;
	
	private Long numProcesso;
	
	private String indGravacao;
	
	private Integer tipoObservacao;
	
	public Long getIdentObservacao() {
		return identObservacao;
	}

	public void setIdentObservacao(Long identObservacao) {
		this.identObservacao = identObservacao;
	}

	public String getGrupoObservacao() {
		return grupoObservacao;
	}

	public void setGrupoObservacao(String grupoObservacao) {
		this.grupoObservacao = grupoObservacao;
	}

	public String getCodObservacao() {
		return codObservacao;
	}

	public void setCodObservacao(String codObservacao) {
		this.codObservacao = codObservacao;
	}

	public YearMonthDay getValidObservacao() {
		return validObservacao;
	}

	public void setValidObservacao(YearMonthDay validObservacao) {
		this.validObservacao = validObservacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(Long numProcesso) {
		this.numProcesso = numProcesso;
	}

	public String getIndGravacao() {
		return indGravacao;
	}

	public void setIndGravacao(String indGravacao) {
		this.indGravacao = indGravacao;
	}

	public Integer getTipoObservacao() {
		return tipoObservacao;
	}

	public void setTipoObservacao(Integer tipoObservacao) {
		this.tipoObservacao = tipoObservacao;
	}
	}
