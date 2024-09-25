package com.mercurio.lms.rest.tabeladeprecos;

import java.io.Serializable;
import java.math.BigDecimal;

public class MarkupValorFaixaProgressivaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String faixa;
	private BigDecimal valor;
	private Long idValorMarkupFaixaProgressiva;
	private Long idFaixaProgressiva;
	
	public MarkupValorFaixaProgressivaDTO() {
	}
	
	public MarkupValorFaixaProgressivaDTO(Long idValorMarkupFaixaProgressiva,
			Long idFaixaProgressiva, String faixa, BigDecimal valorMarkup) {
		this.idValorMarkupFaixaProgressiva = idValorMarkupFaixaProgressiva;
		this.idFaixaProgressiva = idFaixaProgressiva;
		this.faixa = faixa;
		this.valor = valorMarkup;
	}
	public Long getIdValorMarkupFaixaProgressiva() {
		return idValorMarkupFaixaProgressiva;
	}
	public void setIdValorMarkupFaixaProgressiva(Long idValorMarkupFaixaProgressiva) {
		this.idValorMarkupFaixaProgressiva = idValorMarkupFaixaProgressiva;
	}
	public String getFaixa() {
		return faixa;
	}
	public void setFaixa(String faixa) {
		this.faixa = faixa;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Long getIdFaixaProgressiva() {
		return idFaixaProgressiva;
	}
	public void setIdFaixaProgressiva(Long idFaixaProgressiva) {
		this.idFaixaProgressiva = idFaixaProgressiva;
	}
	
}
