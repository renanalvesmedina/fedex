package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.NaturezaProduto;

public class DivisaoClienteLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long idDivisaoClienteLog;
	private DivisaoCliente divisaoCliente;
	private Cliente cliente;
	private String cdDivisaoCliente;
	private String dsDivisaoCliente;
	private Long nrQtdeDocsRomaneio;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
	private NaturezaProduto naturezaProduto;

	public long getIdDivisaoClienteLog() {
		return idDivisaoClienteLog;
	}

	public void setIdDivisaoClienteLog(long idDivisaoClienteLog) {
		this.idDivisaoClienteLog = idDivisaoClienteLog;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCdDivisaoCliente() {
		return cdDivisaoCliente;
	}

	public void setCdDivisaoCliente(String cdDivisaoCliente) {
		this.cdDivisaoCliente = cdDivisaoCliente;
	}

	public String getDsDivisaoCliente() {
		return dsDivisaoCliente;
	}

	public void setDsDivisaoCliente(String dsDivisaoCliente) {
		this.dsDivisaoCliente = dsDivisaoCliente;
	}

	public Long getNrQtdeDocsRomaneio() {
		return nrQtdeDocsRomaneio;
	}

	public void setNrQtdeDocsRomaneio(Long nrQtdeDocsRomaneio) {
		this.nrQtdeDocsRomaneio = nrQtdeDocsRomaneio;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public NaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
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
		return new ToStringBuilder(this).append("idDivisaoClienteLog",
				getIdDivisaoClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DivisaoClienteLog))
			return false;
		DivisaoClienteLog castOther = (DivisaoClienteLog) other;
		return new EqualsBuilder().append(this.getIdDivisaoClienteLog(),
				castOther.getIdDivisaoClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDivisaoClienteLog())
				.toHashCode();
	}
}