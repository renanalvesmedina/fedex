package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

public class FatorCubagemDivisao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFatorCubagemDivisao;

	private BigDecimal nrFatorCubagemReal;

	private YearMonthDay dtVigenciaInicial;
	
	private YearMonthDay dtVigenciaFinal;

	private DivisaoCliente divisaoCliente;

	public Long getIdFatorCubagemDivisao() {
		return idFatorCubagemDivisao;
	}

	public void setIdFatorCubagemDivisao(Long idFatorCubagemDivisao) {
		this.idFatorCubagemDivisao = idFatorCubagemDivisao;
	}

	public BigDecimal getNrFatorCubagemReal() {
		return nrFatorCubagemReal;
	}

	public void setNrFatorCubagemReal(BigDecimal nrFatorCubagemReal) {
		this.nrFatorCubagemReal = nrFatorCubagemReal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}
}
