package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class AgrupamentoClienteLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long idAgrupamentoClienteLog;
	private AgrupamentoCliente agrupamentoCliente;
	private FormaAgrupamento formaAgrupamento;
	private DivisaoCliente divisaoCliente;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;

	public long getIdAgrupamentoClienteLog() {
		return idAgrupamentoClienteLog;
	}

	public void setIdAgrupamentoClienteLog(long idAgrupamentoClienteLog) {
		this.idAgrupamentoClienteLog = idAgrupamentoClienteLog;
	}

	public AgrupamentoCliente getAgrupamentoCliente() {
		return agrupamentoCliente;
	}

	public void setAgrupamentoCliente(AgrupamentoCliente agrupamentoCliente) {
		this.agrupamentoCliente = agrupamentoCliente;
	}

	public FormaAgrupamento getFormaAgrupamento() {
		return formaAgrupamento;
	}

	public void setFormaAgrupamento(FormaAgrupamento formaAgrupamento) {
		this.formaAgrupamento = formaAgrupamento;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
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
		return new ToStringBuilder(this).append("idAgrupamentoClienteLog",
				getIdAgrupamentoClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgrupamentoClienteLog))
			return false;
		AgrupamentoClienteLog castOther = (AgrupamentoClienteLog) other;
		return new EqualsBuilder().append(this.getIdAgrupamentoClienteLog(),
				castOther.getIdAgrupamentoClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAgrupamentoClienteLog())
				.toHashCode();
	}
}