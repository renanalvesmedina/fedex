package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.mercurio.lms.configuracoes.model.Moeda;

public class ServicoAdicionalPrecificado {	
	private static final String TP_INDICADOR_POR_VALOR = "V";
	private static final String TP_INDICADOR_POR_DESCONTO = "D";
	private static final String TP_INDICADOR_POR_ACRESCIMO = "A";
	
	private Long idParcelaPreco;
	private String cdParcela;
	private String dsParcela;
	
	private String tpIndicadorCalculo;
	private BigDecimal vlTabela;
	private BigDecimal vlMinimoTabela;
	
	private String tpIndicadorParametrizacao;
	private BigDecimal vlParametrizacao;
	private BigDecimal vlMinimoParametrizacao;
	
	private Integer qtDiasCarencia;
	private Integer qtDiasDecurso;	
	
	private Boolean isServicoComplementar = Boolean.FALSE;
	
	private Moeda moeda;
	private ServicoAdicionalPrecificado servicoAdicionalComplementar;	
	
	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}
	
	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}
	
	public String getCdParcela() {
		return cdParcela;
	}
	
	public void setCdParcela(String cdParcela) {
		this.cdParcela = cdParcela;
	}
	
	public String getDsParcela() {
		return dsParcela;
	}
	public void setDsParcela(String dsParcela) {
		this.dsParcela = dsParcela;
	}
	
	public String getTpIndicadorCalculo() {
		return tpIndicadorCalculo;
	}
	public void setTpIndicadorCalculo(String tpIndicadorCalculo) {
		this.tpIndicadorCalculo = tpIndicadorCalculo;
	}
	
	public BigDecimal getVlTabela() {
		return vlTabela;
	}
	public void setVlTabela(BigDecimal vlTabela) {
		this.vlTabela = vlTabela;
	}
	
	public BigDecimal getVlMinimoTabela() {
		return vlMinimoTabela;
	}
	public void setVlMinimoTabela(BigDecimal vlMinimoTabela) {
		this.vlMinimoTabela = vlMinimoTabela;
	}
	
	public String getTpIndicadorParametrizacao() {
		return tpIndicadorParametrizacao;
	}
	public void setTpIndicadorParametrizacao(String tpIndicadorParametrizacao) {
		this.tpIndicadorParametrizacao = tpIndicadorParametrizacao;
	}
	
	public BigDecimal getVlParametrizacao() {
		return vlParametrizacao;
	}
	public void setVlParametrizacao(BigDecimal vlParametrizacao) {
		this.vlParametrizacao = vlParametrizacao;
	}
	
	public BigDecimal getVlMinimoParametrizacao() {
		return vlMinimoParametrizacao;
	}
	public void setVlMinimoParametrizacao(BigDecimal vlMinimoParametrizacao) {
		this.vlMinimoParametrizacao = vlMinimoParametrizacao;
	}
	
	public Integer getQtDiasCarencia() {
		return qtDiasCarencia;
	}
	public void setQtDiasCarencia(Integer qtDiasCarencia) {
		this.qtDiasCarencia = qtDiasCarencia;
	}
	
	public Integer getQtDiasDecurso() {
		return qtDiasDecurso;
	}
	public void setQtDiasDecurso(Integer qtDiasDecurso) {
		this.qtDiasDecurso = qtDiasDecurso;
	}
	
	public Boolean getIsServicoComplementar() {
		return isServicoComplementar;
	}
	public void setIsServicoComplementar(Boolean isServicoComplementar) {
		this.isServicoComplementar = isServicoComplementar;
	}
	
	public Moeda getMoeda() {
		return moeda;
	}
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}
	
	public ServicoAdicionalPrecificado getServicoAdicionalComplementar() {
		return servicoAdicionalComplementar;
	}
	
	public void setServicoAdicionalComplementar(ServicoAdicionalPrecificado servicoAdicionalComplementar) {
		this.servicoAdicionalComplementar = servicoAdicionalComplementar;
	}
	
	public String getVlServicoFormatado(String mask, Locale locale) {				
		return getValorFormatado(getVlServico(), mask, locale);
	}
	
	public String getVlMinimoServicoFormatado(String mask, Locale locale) {
		return getValorFormatado(getVlMinimoServico(), mask, locale);
	}
	
	private String getValorFormatado(BigDecimal valor, String mask, Locale locale) {
		if(valor == null) {
			return null;
		}
		
		NumberFormat format = new DecimalFormat(mask, new DecimalFormatSymbols(locale));
		return format.format(valor);
	}
	
	public BigDecimal getVlServico() {
		return getValorParametrizado(vlTabela, vlParametrizacao, tpIndicadorParametrizacao);
	}
	
	public BigDecimal getVlMinimoServico() {
		return getValorParametrizado(vlMinimoTabela, vlMinimoParametrizacao, tpIndicadorParametrizacao);
	}
	
	private BigDecimal getValorParametrizado(BigDecimal valorTabela, BigDecimal valorParametrizacao, String tpIndicador) {
		if(tpIndicador != null && valorParametrizacao != null) {					
			if(TP_INDICADOR_POR_VALOR.equals(tpIndicador)) {
				return getValorArredondado(valorParametrizacao);
			} else if(TP_INDICADOR_POR_DESCONTO.equals(tpIndicador)) {				
				return getValorComDesconto(valorTabela, valorParametrizacao);					
			} else if(TP_INDICADOR_POR_ACRESCIMO.equals(tpIndicador)) {
				return getValorComAcrescimo(valorTabela, valorParametrizacao);
			}		
		}
		
		return getValorArredondado(valorTabela);
	}
	
	private BigDecimal getValorComDesconto(BigDecimal valor, BigDecimal percentualDesconto) {
		if(valor != null) {
			BigDecimal valorPercentual = getValorPercentual(valor, percentualDesconto);
			return getValorArredondado(valor.subtract(valorPercentual));
		}
		return null;		
	}
	
	private BigDecimal getValorComAcrescimo(BigDecimal valor, BigDecimal percentualAcrescimo) {		
		if(valor != null) {
			BigDecimal valorPercentual = getValorPercentual(valor, percentualAcrescimo);		
			return getValorArredondado(valor.add(valorPercentual));
		} 		
		return null;
	}
	
	private BigDecimal getValorPercentual(BigDecimal valor, BigDecimal percentual) {		
		if(valor != null) {
			return valor.multiply(percentual.divide(new BigDecimal(100)));
		}
		return null;
	}
	
	private BigDecimal getValorArredondado(BigDecimal valor) {
		if(valor != null) {
			return valor.setScale(2, BigDecimal.ROUND_HALF_UP);
		} 
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		ServicoAdicionalPrecificado other = (ServicoAdicionalPrecificado) obj;
		if (cdParcela == null) {
			if (other.cdParcela != null)
				return false;
		} else if (!cdParcela.equals(other.cdParcela))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdParcela == null) ? 0 : cdParcela.hashCode());
		return result;
	}
	
	
}
