package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.dto.ReciboFreteCarreteiroSuggestDTO;

public class ParcelaDescontoRfcDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idParcelaDescontoRfc;
	private Integer numeroParcela;
	private YearMonthDay data;
	private BigDecimal valor;
	private String descricaoParcela;

	private ReciboFreteCarreteiroSuggestDTO recibo;

	public ParcelaDescontoRfcDTO(Integer numeroParcela,
			YearMonthDay dataInicio, BigDecimal valorFixo) {
		this.numeroParcela = numeroParcela;
		this.data = dataInicio;
		this.valor = valorFixo;
	}

	public ParcelaDescontoRfcDTO() {
	}

	public Integer getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(Integer numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public YearMonthDay getData() {
		return data;
	}

	public void setData(YearMonthDay data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDescricaoParcela() {
		return descricaoParcela;
	}

	public void setDescricaoParcela(String descricaoParcela) {
		this.descricaoParcela = descricaoParcela;
	}

	public Long getIdParcelaDescontoRfc() {
		return idParcelaDescontoRfc;
	}

	public void setIdParcelaDescontoRfc(Long idParcelaDescontoRfc) {
		this.idParcelaDescontoRfc = idParcelaDescontoRfc;
	}

	public ReciboFreteCarreteiroSuggestDTO getRecibo() {
		return recibo;
	}

	public void setRecibo(ReciboFreteCarreteiroSuggestDTO recibo) {
		this.recibo = recibo;
	}
}
