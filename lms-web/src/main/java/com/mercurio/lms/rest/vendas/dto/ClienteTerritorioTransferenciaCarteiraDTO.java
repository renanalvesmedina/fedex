package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class ClienteTerritorioTransferenciaCarteiraDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private TerritorioSuggestDTO territorioDe;
	private TerritorioSuggestDTO territorioPara;
	private YearMonthDay dtFechamento;
	private YearMonthDay dtInicio;
	
	public TerritorioSuggestDTO getTerritorioDe() {
		return territorioDe;
	}
	public void setTerritorioDe(TerritorioSuggestDTO territorioDe) {
		this.territorioDe = territorioDe;
	}
	public TerritorioSuggestDTO getTerritorioPara() {
		return territorioPara;
	}
	public void setTerritorioPara(TerritorioSuggestDTO territorioPara) {
		this.territorioPara = territorioPara;
	}
	public YearMonthDay getDtFechamento() {
		return dtFechamento;
	}
	public void setDtFechamento(YearMonthDay dtFechamento) {
		this.dtFechamento = dtFechamento;
	}
	public YearMonthDay getDtInicio() {
		return dtInicio;
	}
	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}
}
