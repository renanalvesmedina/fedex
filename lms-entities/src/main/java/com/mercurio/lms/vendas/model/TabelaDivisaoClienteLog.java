package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;

public class TabelaDivisaoClienteLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTabelaDivisaoClienteLog;
	private TabelaDivisaoCliente tabelaDivisaoCliente;
	private Servico servico;
	private DivisaoCliente divisaoCliente;
	private TabelaPreco tabelaPreco;
	private boolean blAtualizacaoAutomatica;
	private BigDecimal pcAumento;
	private boolean blObrigaDimensoes;
	private boolean blPagaFreteTonelada;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTabelaDivisaoClienteLog() {
   
		return idTabelaDivisaoClienteLog;
	}
   
	public void setIdTabelaDivisaoClienteLog(long idTabelaDivisaoClienteLog) {
   
		this.idTabelaDivisaoClienteLog = idTabelaDivisaoClienteLog;
	}
	
	public TabelaDivisaoCliente getTabelaDivisaoCliente() {
   
		return tabelaDivisaoCliente;
	}
   
	public void setTabelaDivisaoCliente(
			TabelaDivisaoCliente tabelaDivisaoCliente) {
   
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
	}
	
	public Servico getServico() {
   
		return servico;
	}
   
	public void setServico(Servico servico) {
   
		this.servico = servico;
	}
	
	public DivisaoCliente getDivisaoCliente() {
   
		return divisaoCliente;
	}
   
	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
   
		this.divisaoCliente = divisaoCliente;
	}
	
	public TabelaPreco getTabelaPreco() {
   
		return tabelaPreco;
	}
   
	public void setTabelaPreco(TabelaPreco tabelaPreco) {
   
		this.tabelaPreco = tabelaPreco;
	}
	
	public boolean isBlAtualizacaoAutomatica() {
   
		return blAtualizacaoAutomatica;
	}
   
	public void setBlAtualizacaoAutomatica(boolean blAtualizacaoAutomatica) {
   
		this.blAtualizacaoAutomatica = blAtualizacaoAutomatica;
	}
	
	public BigDecimal getPcAumento() {
   
		return pcAumento;
	}
   
	public void setPcAumento(BigDecimal pcAumento) {
   
		this.pcAumento = pcAumento;
	}
	
	public boolean isBlObrigaDimensoes() {
   
		return blObrigaDimensoes;
	}
   
	public void setBlObrigaDimensoes(boolean blObrigaDimensoes) {
   
		this.blObrigaDimensoes = blObrigaDimensoes;
	}
	
	public boolean isBlPagaFreteTonelada() {
   
		return blPagaFreteTonelada;
	}
   
	public void setBlPagaFreteTonelada(boolean blPagaFreteTonelada) {
   
		this.blPagaFreteTonelada = blPagaFreteTonelada;
	}
	
	public DomainValue getTpOrigemLog() {
   
		return tpOrigemLog;
	}
   
	public void setTpOrigemLog(DomainValue tpOrigemLog) {
   
		this.tpOrigemLog = tpOrigemLog;
	}
	
	public String getLoginLog() {
   
		return loginLog;
	}
   
	public void setLoginLog(String loginLog) {
   
		this.loginLog = loginLog;
	}
	
	public DateTime getDhLog() {
   
		return dhLog;
	}
   
	public void setDhLog(DateTime dhLog) {
   
		this.dhLog = dhLog;
	}
	
	public DomainValue getOpLog() {
   
		return opLog;
	}
   
	public void setOpLog(DomainValue opLog) {
   
		this.opLog = opLog;
	}
	
   	public String toString() {
		return new ToStringBuilder(this).append("idTabelaDivisaoClienteLog",
				getIdTabelaDivisaoClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TabelaDivisaoClienteLog))
			return false;
		TabelaDivisaoClienteLog castOther = (TabelaDivisaoClienteLog) other;
		return new EqualsBuilder().append(this.getIdTabelaDivisaoClienteLog(),
				castOther.getIdTabelaDivisaoClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTabelaDivisaoClienteLog())
			.toHashCode();
	}
} 