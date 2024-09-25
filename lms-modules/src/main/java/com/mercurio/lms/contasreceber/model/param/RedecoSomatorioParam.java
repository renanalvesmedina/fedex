package com.mercurio.lms.contasreceber.model.param;

import java.math.BigDecimal;


public class RedecoSomatorioParam {

	private Long qtTotalDocumentos;
	
	private BigDecimal vlTotalPago;
	
	private BigDecimal vlTotalJuros;
	
	private BigDecimal vlTotalDesconto;
	
	private BigDecimal vlTotalTarifas;
	
	private BigDecimal vlDiferencaCambialCotacao;

	public Long getQtTotalDocumentos() {
		return qtTotalDocumentos;
	}

	public void setQtTotalDocumentos(Long qtTotalDocumentos) {
		this.qtTotalDocumentos = qtTotalDocumentos;
	}

	public BigDecimal getVlTotalDesconto() {
		return vlTotalDesconto;
	}

	public void setVlTotalDesconto(BigDecimal vlTotalDesconto) {
		this.vlTotalDesconto = vlTotalDesconto;
	}

	public BigDecimal getVlTotalJuros() {
		return vlTotalJuros;
	}

	public void setVlTotalJuros(BigDecimal vlTotalJuros) {
		this.vlTotalJuros = vlTotalJuros;
	}

	public BigDecimal getVlTotalPago() {
		return vlTotalPago;
	}

	public void setVlTotalPago(BigDecimal vlTotalPago) {
		this.vlTotalPago = vlTotalPago;
	}

	public BigDecimal getVlTotalTarifas() {
		return vlTotalTarifas;
	}

	public void setVlTotalTarifas(BigDecimal vlTotalTarifas) {
		this.vlTotalTarifas = vlTotalTarifas;
	}

	public BigDecimal getVlDiferencaCambialCotacao() {
		return vlDiferencaCambialCotacao;
	}

	public void setVlDiferencaCambialCotacao(BigDecimal vlDiferencaCambialCotacao) {
		this.vlDiferencaCambialCotacao = vlDiferencaCambialCotacao;
	}
	

}
