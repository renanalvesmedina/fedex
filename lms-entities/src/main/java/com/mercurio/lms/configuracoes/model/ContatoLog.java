package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class ContatoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idContatoLog;
	private Contato contato;
	private String nmContato;
	private String dsEmail;
	private String dsFuncao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdContatoLog() {
   
		return idContatoLog;
	}
   
	public void setIdContatoLog(long idContatoLog) {
   
		this.idContatoLog = idContatoLog;
	}
	
	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public String getNmContato() {
   
		return nmContato;
	}
   
	public void setNmContato(String nmContato) {
   
		this.nmContato = nmContato;
	}
	
	public String getDsEmail() {
   
		return dsEmail;
	}
   
	public void setDsEmail(String dsEmail) {
   
		this.dsEmail = dsEmail;
	}
	
	public String getDsFuncao() {
   
		return dsFuncao;
	}
   
	public void setDsFuncao(String dsFuncao) {
   
		this.dsFuncao = dsFuncao;
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
		return new ToStringBuilder(this).append("idContatoLog",
				getIdContatoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ContatoLog))
			return false;
		ContatoLog castOther = (ContatoLog) other;
		return new EqualsBuilder().append(this.getIdContatoLog(),
				castOther.getIdContatoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdContatoLog()).toHashCode();
	}
} 