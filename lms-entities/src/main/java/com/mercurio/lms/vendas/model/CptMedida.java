package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

public class CptMedida implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCptMedida;

	private CptComplexidade cptComplexidade;

	private YearMonthDay dtVigenciaInicial;

	private YearMonthDay dtVigenciaFinal;

	private SegmentoMercado segmentoMercado;

	private Cliente cliente;
	
	private BigDecimal vlMedida;

	public Long getIdCptMedida() {
		return idCptMedida;
	}

	public void setIdCptMedida(Long idCptMedida) {
		this.idCptMedida = idCptMedida;
	}

	public CptComplexidade getCptComplexidade() {
		return cptComplexidade;
	}

	public void setCptComplexidade(CptComplexidade cptComplexidade) {
		this.cptComplexidade = cptComplexidade;
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

	public SegmentoMercado getSegmentoMercado() {
		return segmentoMercado;
	}

	public void setSegmentoMercado(SegmentoMercado segmentoMercado) {
		this.segmentoMercado = segmentoMercado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getVlMedida() {
		return vlMedida;
	}

	public void setVlMedida(BigDecimal vlMedida) {
		this.vlMedida = vlMedida;
	}

}
