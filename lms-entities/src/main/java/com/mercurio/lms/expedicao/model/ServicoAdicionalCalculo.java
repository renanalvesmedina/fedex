package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;

public class ServicoAdicionalCalculo {
	private BigDecimal vlMercadoria;
	private BigDecimal vlFrete;
	private BigDecimal psReferencia;
	
	private Integer qtDias;
	private Integer qtPaletes;
	private Integer qtKmRodados;	
	private Integer qtSegurancasAdicionais;
	
	private String cdParcela;
	private BigDecimal vlCusto;
	private BigDecimal vlNegociado;
	
	private BigDecimal vlCalculado;
	
	private BigDecimal vlTabela;
	
	private ServicoAdicionalPrecificado servicoAdicionalPrecificado;
	
	private Boolean blPagaSeguro;
	
	private Long idDivisaoCliente;
	
	private Boolean isTpUnidadeMedidoCobrTonelada;
	
	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}
	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}
	
	public BigDecimal getVlFrete() {
		return vlFrete;
	}
	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}
	
	public BigDecimal getPsReferencia() {
		return psReferencia;
	}
	public void setPsReferencia(BigDecimal psReferencia) {
		this.psReferencia = psReferencia;
	}
	
	public Integer getQtDias() {
		return qtDias;
	}
	public void setQtDias(Integer qtDias) {
		this.qtDias = qtDias;
	}
	
	public Integer getQtPaletes() {
		return qtPaletes;
	}
	public void setQtPaletes(Integer qtPaletes) {
		this.qtPaletes = qtPaletes;
	}
	
	public Integer getQtKmRodados() {
		return qtKmRodados;
	}
	public void setQtKmRodados(Integer qtKmRodados) {
		this.qtKmRodados = qtKmRodados;
	}
	
	public Integer getQtSegurancasAdicionais() {
		return qtSegurancasAdicionais;
	}
	public void setQtSegurancasAdicionais(Integer qtSegurancasAdicionais) {
		this.qtSegurancasAdicionais = qtSegurancasAdicionais;
	}
	
	public String getCdParcela() {
		return cdParcela;
	}
	public void setCdParcela(String cdParcela) {
		this.cdParcela = cdParcela;
	}
	
	public BigDecimal getVlCusto() {
		return vlCusto;
	}
	public void setVlCusto(BigDecimal vlCusto) {
		this.vlCusto = vlCusto;
	}
	
	public BigDecimal getVlNegociado() {
		return vlNegociado;
	}
	public void setVlNegociado(BigDecimal vlNegociado) {
		this.vlNegociado = vlNegociado;
	}
	
	public BigDecimal getVlCalculado() {
		return vlCalculado;
	}
	public void setVlCalculado(BigDecimal vlCalculado) {
		this.vlCalculado = vlCalculado;
	}
	
	public ServicoAdicionalPrecificado getServicoAdicionalPrecificado() {
		return servicoAdicionalPrecificado;
	}
	
	public void setServicoAdicionalPrecificado(
			ServicoAdicionalPrecificado servicoAdicionalPrecificado) {
		this.servicoAdicionalPrecificado = servicoAdicionalPrecificado;
	}

	public Boolean getBlPagaSeguro() {
		return blPagaSeguro;
	}
	
	public void setBlPagaSeguro(Boolean blPagaSeguro) {
		this.blPagaSeguro = blPagaSeguro;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServicoAdicionalCalculo other = (ServicoAdicionalCalculo) obj;
		if (cdParcela == null) {
			if (other.cdParcela != null)
				return false;
		} else if (!cdParcela.equals(other.cdParcela)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdParcela == null) ? 0 : cdParcela.hashCode());
		return result;
	}
	
	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}
	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}
	public Boolean getIsTpUnidadeMedidoCobrTonelada() {
		return isTpUnidadeMedidoCobrTonelada;
	}
	public void setIsTpUnidadeMedidoCobrTonelada(
			Boolean isTpUnidadeMedidoCobrEqualsT) {
		this.isTpUnidadeMedidoCobrTonelada = isTpUnidadeMedidoCobrEqualsT;
	}
	public BigDecimal getVlTabela() {
		return vlTabela;
	}
	public void setVlTabela(BigDecimal vlTabela) {
		this.vlTabela = vlTabela;
	}
}
