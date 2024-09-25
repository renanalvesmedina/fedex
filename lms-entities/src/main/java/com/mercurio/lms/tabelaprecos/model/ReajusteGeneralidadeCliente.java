package com.mercurio.lms.tabelaprecos.model;

import java.math.BigDecimal;

public class ReajusteGeneralidadeCliente {

	private Long  		idGeneralidadeCliente;
	private Long 		idParcelaPreco;
	private BigDecimal  percentualReajuste;
	private BigDecimal  percentualMinReajuste;
	private BigDecimal  valorTabela;
	private BigDecimal  valorMinTabela;
	
	
	public Long getIdGeneralidadeCliente() {
		return idGeneralidadeCliente;
	}
	public void setIdGeneralidadeCliente(Long idGeneralidadeCliente) {
		this.idGeneralidadeCliente = idGeneralidadeCliente;
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
	public BigDecimal getValorTabela() {
		return valorTabela;
	}
	public void setValorTabela(BigDecimal valorTabela) {
		this.valorTabela = valorTabela;
	}
	public BigDecimal getValorMinTabela() {
		return valorMinTabela;
	}
	public void setValorMinTabela(BigDecimal valorMinTabela) {
		this.valorMinTabela = valorMinTabela;
	}
	
}
