package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class TipoProdutoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTipoProdutoLog;
	private TipoProduto tipoProduto;
	private String dsTipoProduto;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTipoProdutoLog() {
   
		return idTipoProdutoLog;
	}
   
	public void setIdTipoProdutoLog(long idTipoProdutoLog) {
   
		this.idTipoProdutoLog = idTipoProdutoLog;
	}
	
	public TipoProduto getTipoProduto() {
   
		return tipoProduto;
	}
   
	public void setTipoProduto(TipoProduto tipoProduto) {
   
		this.tipoProduto = tipoProduto;
	}
	
	public String getDsTipoProduto() {
   
		return dsTipoProduto;
	}
   
	public void setDsTipoProduto(String dsTipoProduto) {
   
		this.dsTipoProduto = dsTipoProduto;
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
		return new ToStringBuilder(this).append("idTipoProdutoLog",
				getIdTipoProdutoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoProdutoLog))
			return false;
		TipoProdutoLog castOther = (TipoProdutoLog) other;
		return new EqualsBuilder().append(this.getIdTipoProdutoLog(),
				castOther.getIdTipoProdutoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoProdutoLog()).toHashCode();
	}
} 