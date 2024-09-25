package com.mercurio.lms.rest.expedicao;

import org.joda.time.DateTime;

public class EventosLocalizacaoDTO {

	private String dataEventoFormatada;
	private String dsTracking;
	private String tpLocalizacao;

	private Long idAwb; 
	private Long idLocalizacao;
	private DateTime dtEvento;
	
	
	public String getDataEventoFormatada() {
		return dataEventoFormatada;
	}
	public void setDataEventoFormatada(String dataEventoFormatada) {
		this.dataEventoFormatada = dataEventoFormatada;
	}
	public String getDsTracking() {
		return dsTracking;
	}
	public void setDsTracking(String dsTracking) {
		this.dsTracking = dsTracking;
	}
	public String getTpLocalizacao() {
		return tpLocalizacao;
	}
	public void setTpLocalizacao(String tpLocalizacao) {
		this.tpLocalizacao = tpLocalizacao;
	}
	public Long getIdAwb() {
		return idAwb;
	}
	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}
	public Long getIdLocalizacao() {
		return idLocalizacao;
	}
	public void setIdLocalizacao(Long idLocalizacao) {
		this.idLocalizacao = idLocalizacao;
	}
	public DateTime getDtEvento() {
		return dtEvento;
	}
	public void setDtEvento(DateTime dtEvento) {
		this.dtEvento = dtEvento;
	}
}
