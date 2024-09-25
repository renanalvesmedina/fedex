package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class NaturezaProdutoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idNaturezaProdutoLog;
	private NaturezaProduto naturezaProduto;
	private String dsNaturezaProduto;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdNaturezaProdutoLog() {
   
		return idNaturezaProdutoLog;
	}
   
	public void setIdNaturezaProdutoLog(long idNaturezaProdutoLog) {
   
		this.idNaturezaProdutoLog = idNaturezaProdutoLog;
	}
	
	public NaturezaProduto getNaturezaProduto() {
   
		return naturezaProduto;
	}
   
	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
   
		this.naturezaProduto = naturezaProduto;
	}
	
	public String getDsNaturezaProduto() {
   
		return dsNaturezaProduto;
	}
   
	public void setDsNaturezaProduto(String dsNaturezaProduto) {
   
		this.dsNaturezaProduto = dsNaturezaProduto;
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
		return new ToStringBuilder(this).append("idNaturezaProdutoLog",
				getIdNaturezaProdutoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NaturezaProdutoLog))
			return false;
		NaturezaProdutoLog castOther = (NaturezaProdutoLog) other;
		return new EqualsBuilder().append(this.getIdNaturezaProdutoLog(),
				castOther.getIdNaturezaProdutoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdNaturezaProdutoLog())
			.toHashCode();
	}
} 