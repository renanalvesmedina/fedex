package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class ObservacaoConhecimentoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idObservacaoConhecimentoLog;
	private ObservacaoConhecimento observacaoConhecimento;
	private String dsObservacaoConhecimento;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdObservacaoConhecimentoLog() {
   
		return idObservacaoConhecimentoLog;
	}
   
	public void setIdObservacaoConhecimentoLog(long idObservacaoConhecimentoLog) {
   
		this.idObservacaoConhecimentoLog = idObservacaoConhecimentoLog;
	}
	
	public ObservacaoConhecimento getObservacaoConhecimento() {
   
		return observacaoConhecimento;
	}
   
	public void setObservacaoConhecimento(
			ObservacaoConhecimento observacaoConhecimento) {
   
		this.observacaoConhecimento = observacaoConhecimento;
	}
	
	public String getDsObservacaoConhecimento() {
   
		return dsObservacaoConhecimento;
	}
   
	public void setDsObservacaoConhecimento(String dsObservacaoConhecimento) {
   
		this.dsObservacaoConhecimento = dsObservacaoConhecimento;
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
		return new ToStringBuilder(this).append("idObservacaoConhecimentoLog",
				getIdObservacaoConhecimentoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ObservacaoConhecimentoLog))
			return false;
		ObservacaoConhecimentoLog castOther = (ObservacaoConhecimentoLog) other;
		return new EqualsBuilder().append(
				this.getIdObservacaoConhecimentoLog(),
				castOther.getIdObservacaoConhecimentoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdObservacaoConhecimentoLog())
			.toHashCode();
	}
} 