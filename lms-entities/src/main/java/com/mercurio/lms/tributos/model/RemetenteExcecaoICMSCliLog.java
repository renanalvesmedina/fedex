package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class RemetenteExcecaoICMSCliLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idRemetenteExcecaoICMSCliLog;
	private RemetenteExcecaoICMSCli remetenteExcecaoIcmsCli;
	private ExcecaoICMSCliente excecaoIcmsCliente;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private String nrCnpjParcialRem;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdRemetenteExcecaoICMSCliLog() {
   
		return idRemetenteExcecaoICMSCliLog;
	}
   
	public void setIdRemetenteExcecaoICMSCliLog(
			long idRemetenteExcecaoICMSCliLog) {
   
		this.idRemetenteExcecaoICMSCliLog = idRemetenteExcecaoICMSCliLog;
	}
	
	public RemetenteExcecaoICMSCli getRemetenteExcecaoIcmsCli() {
   
		return remetenteExcecaoIcmsCli;
	}
   
	public void setRemetenteExcecaoIcmsCli(
			RemetenteExcecaoICMSCli remetenteExcecaoIcmsCli) {
   
		this.remetenteExcecaoIcmsCli = remetenteExcecaoIcmsCli;
	}
	
	public ExcecaoICMSCliente getExcecaoIcmsCliente() {
   
		return excecaoIcmsCliente;
	}
   
	public void setExcecaoIcmsCliente(ExcecaoICMSCliente excecaoIcmsCliente) {
   
		this.excecaoIcmsCliente = excecaoIcmsCliente;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public String getNrCnpjParcialRem() {
   
		return nrCnpjParcialRem;
	}
   
	public void setNrCnpjParcialRem(String nrCnpjParcialRem) {
   
		this.nrCnpjParcialRem = nrCnpjParcialRem;
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
		return new ToStringBuilder(this).append("idRemetenteExcecaoICMSCliLog",
				getIdRemetenteExcecaoICMSCliLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RemetenteExcecaoICMSCliLog))
			return false;
		RemetenteExcecaoICMSCliLog castOther = (RemetenteExcecaoICMSCliLog) other;
		return new EqualsBuilder().append(
				this.getIdRemetenteExcecaoICMSCliLog(),
				castOther.getIdRemetenteExcecaoICMSCliLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdRemetenteExcecaoICMSCliLog())
			.toHashCode();
	}
} 