package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class DadosTabelaPrecoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpTipoTabelaPreco;
	private String nrVersao;
	private DomainValue tpSubtipoTabelaPreco;

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public DomainValue getTpTipoTabelaPreco() {
		return tpTipoTabelaPreco;
	}

	public void setTpTipoTabelaPreco(DomainValue tpTipoTabelaPreco) {
		this.tpTipoTabelaPreco = tpTipoTabelaPreco;
	}

	public String getNrVersao() {
		return nrVersao;
	}

	public void setNrVersao(String nrVersao) {
		this.nrVersao = nrVersao;
	}

	public DomainValue getTpSubtipoTabelaPreco() {
		return tpSubtipoTabelaPreco;
	}

	public void setTpSubtipoTabelaPreco(DomainValue tpSubtipoTabelaPreco) {
		this.tpSubtipoTabelaPreco = tpSubtipoTabelaPreco;
	}

	public String toFormattedTabelaPreco() {
		return tpTipoTabelaPreco.getValue() + nrVersao + "-" + tpSubtipoTabelaPreco.getValue();
	}
}
