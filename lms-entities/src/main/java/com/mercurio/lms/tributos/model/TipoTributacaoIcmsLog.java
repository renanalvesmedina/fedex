package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class TipoTributacaoIcmsLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTipoTributacaoIcmsLog;
	private TipoTributacaoIcms tipoTributacaoIcms;
	private String dsTipoTributacaoIcms;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTipoTributacaoIcmsLog() {
   
		return idTipoTributacaoIcmsLog;
	}
   
	public void setIdTipoTributacaoIcmsLog(long idTipoTributacaoIcmsLog) {
   
		this.idTipoTributacaoIcmsLog = idTipoTributacaoIcmsLog;
	}
	
	public TipoTributacaoIcms getTipoTributacaoIcms() {
   
		return tipoTributacaoIcms;
	}
   
	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
   
		this.tipoTributacaoIcms = tipoTributacaoIcms;
	}
	
	public String getDsTipoTributacaoIcms() {
   
		return dsTipoTributacaoIcms;
	}
   
	public void setDsTipoTributacaoIcms(String dsTipoTributacaoIcms) {
   
		this.dsTipoTributacaoIcms = dsTipoTributacaoIcms;
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
		return new ToStringBuilder(this).append("idTipoTributacaoIcmsLog",
				getIdTipoTributacaoIcmsLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoTributacaoIcmsLog))
			return false;
		TipoTributacaoIcmsLog castOther = (TipoTributacaoIcmsLog) other;
		return new EqualsBuilder().append(this.getIdTipoTributacaoIcmsLog(),
				castOther.getIdTipoTributacaoIcmsLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoTributacaoIcmsLog())
			.toHashCode();
	}
} 