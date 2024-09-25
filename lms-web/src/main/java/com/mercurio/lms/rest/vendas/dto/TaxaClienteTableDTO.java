package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.rest.BaseDTO;

public class TaxaClienteTableDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idTaxaCliente;
	private String nmParcelaPreco;
	private String tpTaxaIndicador;
	private String vlTaxa;
	private BigDecimal psMinimo;
	private BigDecimal vlExcedente;

	public Long getIdTaxaCliente() {
		return idTaxaCliente;
	}

	public void setIdTaxaCliente(Long idTaxaCliente) {
		this.idTaxaCliente = idTaxaCliente;
	}

	public String getNmParcelaPreco() {
		return nmParcelaPreco;
	}

	public void setNmParcelaPreco(String nmParcelaPreco) {
		this.nmParcelaPreco = nmParcelaPreco;
	}

	public String getTpTaxaIndicador() {
		return tpTaxaIndicador;
	}

	public void setTpTaxaIndicador(String tpTaxaIndicador) {
		this.tpTaxaIndicador = tpTaxaIndicador;
	}

	public String getVlTaxa() {
		return vlTaxa;
	}

	public void setVlTaxa(String vlTaxa) {
		this.vlTaxa = vlTaxa;
	}

	public BigDecimal getPsMinimo() {
		return psMinimo;
	}

	public void setPsMinimo(BigDecimal psMinimo) {
		this.psMinimo = psMinimo;
	}

	public BigDecimal getVlExcedente() {
		return vlExcedente;
	}

	public void setVlExcedente(BigDecimal vlExcedente) {
		this.vlExcedente = vlExcedente;
	}
}
