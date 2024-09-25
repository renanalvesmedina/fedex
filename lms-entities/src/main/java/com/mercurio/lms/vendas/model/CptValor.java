package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

public class CptValor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCptValor;
	
	private BigDecimal vlValor;
	
	private YearMonthDay dtVigenciaInicial;
	
	private YearMonthDay dtVigenciaFinal;
	
	private Cliente cliente;
	
	private CptTipoValor cptTipoValor;

	public Long getIdCptValor() {
		return idCptValor;
	}

	public void setIdCptValor(Long idCptValor) {
		this.idCptValor = idCptValor;
	}

	public BigDecimal getVlValor() {
		return vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CptTipoValor getCptTipoValor() {
		return cptTipoValor;
	}

	public void setCptTipoValor(CptTipoValor cptTipoValor) {
		this.cptTipoValor = cptTipoValor;
	}

}
