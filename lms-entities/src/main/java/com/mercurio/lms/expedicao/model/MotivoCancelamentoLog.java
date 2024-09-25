package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class MotivoCancelamentoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idMotivoCancelamentoLog;
	private MotivoCancelamento motivoCancelamento;
	private String dsMotivoCancelamento;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdMotivoCancelamentoLog() {
   
		return idMotivoCancelamentoLog;
	}
   
	public void setIdMotivoCancelamentoLog(long idMotivoCancelamentoLog) {
   
		this.idMotivoCancelamentoLog = idMotivoCancelamentoLog;
	}
	
	public MotivoCancelamento getMotivoCancelamento() {
   
		return motivoCancelamento;
	}
   
	public void setMotivoCancelamento(MotivoCancelamento motivoCancelamento) {
   
		this.motivoCancelamento = motivoCancelamento;
	}
	
	public String getDsMotivoCancelamento() {
   
		return dsMotivoCancelamento;
	}
   
	public void setDsMotivoCancelamento(String dsMotivoCancelamento) {
   
		this.dsMotivoCancelamento = dsMotivoCancelamento;
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
		return new ToStringBuilder(this).append("idMotivoCancelamentoLog",
				getIdMotivoCancelamentoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoCancelamentoLog))
			return false;
		MotivoCancelamentoLog castOther = (MotivoCancelamentoLog) other;
		return new EqualsBuilder().append(this.getIdMotivoCancelamentoLog(),
				castOther.getIdMotivoCancelamentoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoCancelamentoLog())
			.toHashCode();
	}
} 