package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class EmbalagemLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idEmbalagemLog;
	private Embalagem embalagem;
	private Long nrAltura;
	private Long nrLargura;
	private Long nrComprimento;
	private String dsEmbalagem;
	private boolean blPrecificada;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdEmbalagemLog() {
   
		return idEmbalagemLog;
	}
   
	public void setIdEmbalagemLog(long idEmbalagemLog) {
   
		this.idEmbalagemLog = idEmbalagemLog;
	}
	
	public Embalagem getEmbalagem() {
   
		return embalagem;
	}
   
	public void setEmbalagem(Embalagem embalagem) {
   
		this.embalagem = embalagem;
	}
	
	public Long getNrAltura() {
   
		return nrAltura;
	}
   
	public void setNrAltura(Long nrAltura) {
   
		this.nrAltura = nrAltura;
	}
	
	public Long getNrLargura() {
   
		return nrLargura;
	}
   
	public void setNrLargura(Long nrLargura) {
   
		this.nrLargura = nrLargura;
	}
	
	public Long getNrComprimento() {
   
		return nrComprimento;
	}
   
	public void setNrComprimento(Long nrComprimento) {
   
		this.nrComprimento = nrComprimento;
	}
	
	public String getDsEmbalagem() {
   
		return dsEmbalagem;
	}
   
	public void setDsEmbalagem(String dsEmbalagem) {
   
		this.dsEmbalagem = dsEmbalagem;
	}
	
	public boolean isBlPrecificada() {
   
		return blPrecificada;
	}
   
	public void setBlPrecificada(boolean blPrecificada) {
   
		this.blPrecificada = blPrecificada;
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
		return new ToStringBuilder(this).append("idEmbalagemLog",
				getIdEmbalagemLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EmbalagemLog))
			return false;
		EmbalagemLog castOther = (EmbalagemLog) other;
		return new EqualsBuilder().append(this.getIdEmbalagemLog(),
				castOther.getIdEmbalagemLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdEmbalagemLog()).toHashCode();
	}
} 