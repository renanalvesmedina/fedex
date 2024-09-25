package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

public class Safx2009 implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String codObservacao;
	
	private String dataX2009;
	
	private String descricao;
	
	private String datGravacao;
	
	private String indAutoCotepe;
	
	private int tipoObservacao;
	
	public String getCodObservacao() {
		return codObservacao;
	}

	public void setCodObservacao(String codObservacao) {
		this.codObservacao = codObservacao;
	}

	public String getDataX2009() {
		return dataX2009;
	}

	public void setDataX2009(String dataX2009) {
		this.dataX2009 = dataX2009;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDatGravacao() {
		return datGravacao;
	}

	public void setDatGravacao(String datGravacao) {
		this.datGravacao = datGravacao;
	}

	public String getIndAutoCotepe() {
		return indAutoCotepe;
	}

	public void setIndAutoCotepe(String indAutoCotepe) {
		this.indAutoCotepe = indAutoCotepe;
	}

	public int getTipoObservacao() {
		return tipoObservacao;
	}

	public void setTipoObservacao(int tipoObservacao) {
		this.tipoObservacao = tipoObservacao;
	}

}
