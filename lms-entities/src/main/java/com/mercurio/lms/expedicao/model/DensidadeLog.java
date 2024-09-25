package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Empresa;

public class DensidadeLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long idDensidadeLog;
	private Densidade densidade;
	private Empresa empresa;
	private BigDecimal vlFator;
	private DomainValue tpDensidade;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;

	public long getIdDensidadeLog() {
		return idDensidadeLog;
	}

	public void setIdDensidadeLog(long idDensidadeLog) {
		this.idDensidadeLog = idDensidadeLog;
	}

	public Densidade getDensidade() {
		return densidade;
	}

	public void setDensidade(Densidade densidade) {
		this.densidade = densidade;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public BigDecimal getVlFator() {
		return vlFator;
	}

	public void setVlFator(BigDecimal vlFator) {
		this.vlFator = vlFator;
	}

	public DomainValue getTpDensidade() {
		return tpDensidade;
	}

	public void setTpDensidade(DomainValue tpDensidade) {
		this.tpDensidade = tpDensidade;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
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
		return new ToStringBuilder(this).append("idDensidadeLog",
				getIdDensidadeLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DensidadeLog))
			return false;
		DensidadeLog castOther = (DensidadeLog) other;
		return new EqualsBuilder().append(this.getIdDensidadeLog(),
				castOther.getIdDensidadeLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDensidadeLog()).toHashCode();
	}
}