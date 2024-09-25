package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class FilialEmbarcadoraLog implements Serializable {
	private static final long serialVersionUID = 1L;

	private long idFilialEmbarcadoraLog;
	private FilialEmbarcadora filialEmbarcadora;
	private Cliente cliente;
	private Filial filial;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;

	public long getIdFilialEmbarcadoraLog() {
		return idFilialEmbarcadoraLog;
	}

	public void setIdFilialEmbarcadoraLog(long idFilialEmbarcadoraLog) {
		this.idFilialEmbarcadoraLog = idFilialEmbarcadoraLog;
	}

	public FilialEmbarcadora getFilialEmbarcadora() {
		return filialEmbarcadora;
	}

	public void setFilialEmbarcadora(FilialEmbarcadora filialEmbarcadora) {
		this.filialEmbarcadora = filialEmbarcadora;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
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
		return new ToStringBuilder(this).append("idFilialEmbarcadoraLog",
				getIdFilialEmbarcadoraLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialEmbarcadoraLog))
			return false;
		FilialEmbarcadoraLog castOther = (FilialEmbarcadoraLog) other;
		return new EqualsBuilder().append(this.getIdFilialEmbarcadoraLog(),
				castOther.getIdFilialEmbarcadoraLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialEmbarcadoraLog())
				.toHashCode();
	}
}