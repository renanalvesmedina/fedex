package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class TaxaClienteDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idParametroCliente;
	private Long idSimulacao;
	private Long idParcelaPreco;
	private DomainValue tpIndicador;
	private BigDecimal vlValor;
	private BigDecimal psMinimo;
	private BigDecimal vlExcedente;

	public Long getIdParametroCliente() {
		return idParametroCliente;
	}

	public void setIdParametroCliente(Long idParametroCliente) {
		this.idParametroCliente = idParametroCliente;
	}

	public Long getIdSimulacao() {
		return idSimulacao;
	}

	public void setIdSimulacao(Long idSimulacao) {
		this.idSimulacao = idSimulacao;
	}

	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}

	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}

	public DomainValue getTpIndicador() {
		return tpIndicador;
	}

	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}

	public BigDecimal getVlValor() {
		return vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
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
