package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class ComissaoGarantidaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private BigDecimal vlComissao;
	private YearMonthDay dtInicio;
	private YearMonthDay dtFim;
	private ExecutivoTerritorioDTO executivoTerritorio;

	public BigDecimal getVlComissao() {
		return vlComissao;
	}

	public void setVlComissao(BigDecimal vlComissao) {
		this.vlComissao = vlComissao;
	}

	public YearMonthDay getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}

	public YearMonthDay getDtFim() {
		return dtFim;
	}

	public void setDtFim(YearMonthDay dtFim) {
		this.dtFim = dtFim;
	}

	public ExecutivoTerritorioDTO getExecutivoTerritorio() {
		return executivoTerritorio;
	}

	public void setExecutivoTerritorio(ExecutivoTerritorioDTO executivoTerritorio) {
		this.executivoTerritorio = executivoTerritorio;
	}

}
