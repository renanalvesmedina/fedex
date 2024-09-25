package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class TelefoneEnderecoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTelefoneEnderecoLog;
	private TelefoneEndereco telefoneEndereco;
	private DomainValue tpUso;
	private String nrDdd;
	private String nrTelefone;
	private String nrDdi;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTelefoneEnderecoLog() {
   
		return idTelefoneEnderecoLog;
	}
   
	public void setIdTelefoneEnderecoLog(long idTelefoneEnderecoLog) {
   
		this.idTelefoneEnderecoLog = idTelefoneEnderecoLog;
	}
	
	public TelefoneEndereco getTelefoneEndereco() {
		return telefoneEndereco;
	}

	public void setTelefoneEndereco(TelefoneEndereco telefoneEndereco) {
		this.telefoneEndereco = telefoneEndereco;
	}

	public DomainValue getTpUso() {
   
		return tpUso;
	}
   
	public void setTpUso(DomainValue tpUso) {
   
		this.tpUso = tpUso;
	}
	
	public String getNrDdd() {
   
		return nrDdd;
	}
   
	public void setNrDdd(String nrDdd) {
   
		this.nrDdd = nrDdd;
	}
	
	public String getNrTelefone() {
   
		return nrTelefone;
	}
   
	public void setNrTelefone(String nrTelefone) {
   
		this.nrTelefone = nrTelefone;
	}
	
	public String getNrDdi() {
   
		return nrDdi;
	}
   
	public void setNrDdi(String nrDdi) {
   
		this.nrDdi = nrDdi;
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
		return new ToStringBuilder(this).append("idTelefoneEnderecoLog",
				getIdTelefoneEnderecoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TelefoneEnderecoLog))
			return false;
		TelefoneEnderecoLog castOther = (TelefoneEnderecoLog) other;
		return new EqualsBuilder().append(this.getIdTelefoneEnderecoLog(),
				castOther.getIdTelefoneEnderecoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTelefoneEnderecoLog())
			.toHashCode();
	}
} 