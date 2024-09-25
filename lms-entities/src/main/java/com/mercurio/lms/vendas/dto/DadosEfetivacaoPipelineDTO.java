package com.mercurio.lms.vendas.dto;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class DadosEfetivacaoPipelineDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private YearMonthDay dtEvento;
	private String dsEvento;
	
	public YearMonthDay getDtEvento() {
		return dtEvento;
	}
	public void setDtEvento(YearMonthDay dtEvento) {
		this.dtEvento = dtEvento;
	}
	public String getDsEvento() {
		return dsEvento;
	}
	public void setDsEvento(String dsEvento) {
		this.dsEvento = dsEvento;
	}
	
}