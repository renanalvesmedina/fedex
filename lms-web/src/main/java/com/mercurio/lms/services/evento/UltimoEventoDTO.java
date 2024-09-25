package com.mercurio.lms.services.evento;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class UltimoEventoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private YearMonthDay dtPrevEntrega;
	private String dsLocalizacaoMercadoria;
	
	public YearMonthDay getDtPrevEntrega() {
		return dtPrevEntrega;
	}
	public void setDtPrevEntrega(YearMonthDay dtPrevEntrega) {
		this.dtPrevEntrega = dtPrevEntrega;
	}
	public String getDsLocalizacaoMercadoria() {
		return dsLocalizacaoMercadoria;
	}
	public void setDsLocalizacaoMercadoria(String dsLocalizacaoMercadoria) {
		this.dsLocalizacaoMercadoria = dsLocalizacaoMercadoria;
	}
	

}
