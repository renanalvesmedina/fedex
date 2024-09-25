package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.municipios.model.Filial;

public class ComposicaoPagamentoRedeco implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long idComposicaoPagamentoRedeco;
	
	private DomainValue tpComposicaoPagamentoRedeco;
	
	private YearMonthDay dtPagamento;
	
	private BigDecimal vlPagamento;
	
	private String obComposicaoPagamentoRedeco;
	
	private Redeco redeco;
	
	private Banco banco;

    private Filial filial;
    
    private CreditoBancarioEntity creditoBancario;

    private Integer numeroDeParcelas;

	public Long getIdComposicaoPagamentoRedeco() {
		return idComposicaoPagamentoRedeco;
	}

	public void setIdComposicaoPagamentoRedeco(Long idComposicaoPagamentoRedeco) {
		this.idComposicaoPagamentoRedeco = idComposicaoPagamentoRedeco;
	}

	public DomainValue getTpComposicaoPagamentoRedeco() {
		return tpComposicaoPagamentoRedeco;
	}

	public void setTpComposicaoPagamentoRedeco(
			DomainValue tpComposicaoPagamentoRedeco) {
		this.tpComposicaoPagamentoRedeco = tpComposicaoPagamentoRedeco;
	}

	public YearMonthDay getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(YearMonthDay dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public BigDecimal getVlPagamento() {
		return vlPagamento;
	}

	public void setVlPagamento(BigDecimal vlPagamento) {
		this.vlPagamento = vlPagamento;
	}

	public String getObComposicaoPagamentoRedeco() {
		return obComposicaoPagamentoRedeco;
	}

	public void setObComposicaoPagamentoRedeco(
			String obComposicaoPagamentoRedeco) {
		this.obComposicaoPagamentoRedeco = obComposicaoPagamentoRedeco;
	}

	public Redeco getRedeco() {
		return redeco;
	}

	public void setRedeco(Redeco redeco) {
		this.redeco = redeco;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public CreditoBancarioEntity getCreditoBancario() {
		return creditoBancario;
	}

	public void setCreditoBancario(CreditoBancarioEntity creditoBancario) {
		this.creditoBancario = creditoBancario;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}
	

}
