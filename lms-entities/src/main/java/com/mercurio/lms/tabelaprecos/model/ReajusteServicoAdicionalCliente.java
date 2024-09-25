package com.mercurio.lms.tabelaprecos.model;

import java.math.BigDecimal;

public class ReajusteServicoAdicionalCliente {

	private Long  		idServAdicionalCliente;
	private Long 		idParcelaPreco;
	private BigDecimal  percentualReajuste;
	private BigDecimal  percentualMinReajuste;
	private BigDecimal  valorReajuste;
	private BigDecimal  valorMinReajuste;
	private String 		tpIndicador;
	
	
	public Long getIdServAdicionalCliente() {
		return idServAdicionalCliente;
	}
	public void setIdServAdicionalCliente(Long idServAdicionalCliente) {
		this.idServAdicionalCliente = idServAdicionalCliente;
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
	public BigDecimal getValorReajuste() {
		return valorReajuste;
	}
	public void setValorReajuste(BigDecimal valorReajuste) {
		this.valorReajuste = valorReajuste;
	}
	public BigDecimal getValorMinReajuste() {
		return valorMinReajuste;
	}
	public void setValorMinReajuste(BigDecimal valorMinReajuste) {
		this.valorMinReajuste = valorMinReajuste;
	}
	public String getTpIndicador() {
		return tpIndicador;
	}
	public void setTpIndicador(String tpIndicador) {
		this.tpIndicador = tpIndicador;
	}
}
