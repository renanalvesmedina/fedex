package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

import javax.persistence.Column;

public class TabelaFcFaixaPesoRestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idTabelaFcFaixaPeso;
	private TabelaFcValoresRestDTO tabelaFcValores;
	private DomainValue tpFator;
	private BigDecimal psInicial;
	private BigDecimal psFinal;
	private BigDecimal vlValor;
	private DomainValue tpFatorSegundo;
	private BigDecimal vlValorSegundo;
	private Boolean blCalculoFaixaUnica;

	public Long getIdTabelaFcFaixaPeso() {
		return this.idTabelaFcFaixaPeso;
	}

	public void setIdTabelaFcFaixaPeso(Long idTabelaFcFaixaPeso) {
		this.idTabelaFcFaixaPeso = idTabelaFcFaixaPeso;
	}

	public TabelaFcValoresRestDTO getTabelaFcValores() {
		return this.tabelaFcValores;
	}

	public void setTabelaFcValores(TabelaFcValoresRestDTO tabelaFcValores) {
		this.tabelaFcValores = tabelaFcValores;
	}

	public DomainValue getTpFator() {
		return this.tpFator;
	}

	public void setTpFator(DomainValue tpFator) {
		this.tpFator = tpFator;
	}

	public BigDecimal getPsInicial() {
		return this.psInicial;
	}

	public void setPsInicial(BigDecimal psInicial) {
		this.psInicial = psInicial;
	}

	public BigDecimal getPsFinal() {
		return this.psFinal;
	}

	public void setPsFinal(BigDecimal psFinal) {
		this.psFinal = psFinal;
	}

	public BigDecimal getVlValor() {
		return this.vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
	}

	public DomainValue getTpFatorSegundo() {
		return tpFatorSegundo;
	}

	public void setTpFatorSegundo(DomainValue tpFatorSegundo) {
		this.tpFatorSegundo = tpFatorSegundo;
	}

	public BigDecimal getVlValorSegundo() {
		return vlValorSegundo;
	}

	public void setVlValorSegundo(BigDecimal vlValorSegundo) {
		this.vlValorSegundo = vlValorSegundo;
	}

	public Boolean getBlCalculoFaixaUnica() {
		return blCalculoFaixaUnica;
	}

	public void setBlCalculoFaixaUnica(Boolean blCalculoFaixaUnica) {
		this.blCalculoFaixaUnica = blCalculoFaixaUnica;
	}
}
