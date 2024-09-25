package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import java.math.BigDecimal;

public class TabelaNotaCreditoParcelasCeDto {
	
	private String tpFatorFretePeso = "";
	private String dsFaixa = "";
	private BigDecimal vlFretePeso = new BigDecimal(0);
	
	public String getTpFatorFretePeso() {
		return tpFatorFretePeso;
	}
	public void setTpFatorFretePeso(String tpFatorFretePeso) {
		this.tpFatorFretePeso = tpFatorFretePeso;
	}
	public String getDsFaixa() {
		return dsFaixa;
	}
	public void setDsFaixa(String dsFaixa) {
		this.dsFaixa = dsFaixa;
	}
	public BigDecimal getVlFretePeso() {
		return vlFretePeso;
	}
	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}
	
}
