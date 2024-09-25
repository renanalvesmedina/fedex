package com.mercurio.lms.tabelaprecos.model;

import java.math.BigDecimal;

public class ReajusteTaxaCliente {

	private Long  		idTaxaCliente;
	private Long 		idParcelaPreco;
	private BigDecimal  percentualReajuste;
	private BigDecimal  percentualMinReajuste;
	
	public Long getIdTaxaCliente() {
		return idTaxaCliente;
	}
	public void setIdTaxaCliente(Long idTaxaCliente) {
		this.idTaxaCliente = idTaxaCliente;
	}
	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}
	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}
	public BigDecimal getPercentualReajuste() {
		return percentualReajuste;
	}
	public void setPercentualReajuste(BigDecimal percentualReajuste) {
		this.percentualReajuste = percentualReajuste;
	}
	public BigDecimal getPercentualMinReajuste() {
		return percentualMinReajuste;
	}
	public void setPercentualMinReajuste(BigDecimal percentualMinReajuste) {
		this.percentualMinReajuste = percentualMinReajuste;
	}
	
	
}
