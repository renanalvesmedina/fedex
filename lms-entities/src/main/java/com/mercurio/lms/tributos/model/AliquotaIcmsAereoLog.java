package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class AliquotaIcmsAereoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idAliquotaIcmsAereoLog;
	private AliquotaIcmsAereo aliquotaIcmsAereo;
	private UnidadeFederativa unidadeFederativa;
	private YearMonthDay dtInicioVigencia;
	private BigDecimal pcAliquotaInterna;
	private BigDecimal pcEmbuteInterno;
	private BigDecimal pcAliquotaInterestadual;
	private BigDecimal pcEmbuteInterestadual;
	private String obInterno;
	private String obInterestadual;
	private BigDecimal pcAliquotaDestNc;
	private BigDecimal pcEmbuteDestNc;
	private String obDestNc;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdAliquotaIcmsAereoLog() {
   
		return idAliquotaIcmsAereoLog;
	}
   
	public void setIdAliquotaIcmsAereoLog(long idAliquotaIcmsAereoLog) {
   
		this.idAliquotaIcmsAereoLog = idAliquotaIcmsAereoLog;
	}
	
	public AliquotaIcmsAereo getAliquotaIcmsAereo() {
   
		return aliquotaIcmsAereo;
	}
   
	public void setAliquotaIcmsAereo(AliquotaIcmsAereo aliquotaIcmsAereo) {
   
		this.aliquotaIcmsAereo = aliquotaIcmsAereo;
	}
	
	public UnidadeFederativa getUnidadeFederativa() {
   
		return unidadeFederativa;
	}
   
	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
   
		this.unidadeFederativa = unidadeFederativa;
	}
	
	public YearMonthDay getDtInicioVigencia() {
   
		return dtInicioVigencia;
	}
   
	public void setDtInicioVigencia(YearMonthDay dtInicioVigencia) {
   
		this.dtInicioVigencia = dtInicioVigencia;
	}
	
	public BigDecimal getPcAliquotaInterna() {
   
		return pcAliquotaInterna;
	}
   
	public void setPcAliquotaInterna(BigDecimal pcAliquotaInterna) {
   
		this.pcAliquotaInterna = pcAliquotaInterna;
	}
	
	public BigDecimal getPcEmbuteInterno() {
   
		return pcEmbuteInterno;
	}
   
	public void setPcEmbuteInterno(BigDecimal pcEmbuteInterno) {
   
		this.pcEmbuteInterno = pcEmbuteInterno;
	}
	
	public BigDecimal getPcAliquotaInterestadual() {
   
		return pcAliquotaInterestadual;
	}
   
	public void setPcAliquotaInterestadual(BigDecimal pcAliquotaInterestadual) {
   
		this.pcAliquotaInterestadual = pcAliquotaInterestadual;
	}
	
	public BigDecimal getPcEmbuteInterestadual() {
   
		return pcEmbuteInterestadual;
	}
   
	public void setPcEmbuteInterestadual(BigDecimal pcEmbuteInterestadual) {
   
		this.pcEmbuteInterestadual = pcEmbuteInterestadual;
	}
	
	public String getObInterno() {
   
		return obInterno;
	}
   
	public void setObInterno(String obInterno) {
   
		this.obInterno = obInterno;
	}
	
	public String getObInterestadual() {
   
		return obInterestadual;
	}
   
	public void setObInterestadual(String obInterestadual) {
   
		this.obInterestadual = obInterestadual;
	}
	
	public BigDecimal getPcAliquotaDestNc() {
   
		return pcAliquotaDestNc;
	}
   
	public void setPcAliquotaDestNc(BigDecimal pcAliquotaDestNc) {
   
		this.pcAliquotaDestNc = pcAliquotaDestNc;
	}
	
	public BigDecimal getPcEmbuteDestNc() {
   
		return pcEmbuteDestNc;
	}
   
	public void setPcEmbuteDestNc(BigDecimal pcEmbuteDestNc) {
   
		this.pcEmbuteDestNc = pcEmbuteDestNc;
	}
	
	public String getObDestNc() {
   
		return obDestNc;
	}
   
	public void setObDestNc(String obDestNc) {
   
		this.obDestNc = obDestNc;
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
		return new ToStringBuilder(this).append("idAliquotaIcmsAereoLog",
				getIdAliquotaIcmsAereoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIcmsAereoLog))
			return false;
		AliquotaIcmsAereoLog castOther = (AliquotaIcmsAereoLog) other;
		return new EqualsBuilder().append(this.getIdAliquotaIcmsAereoLog(),
				castOther.getIdAliquotaIcmsAereoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaIcmsAereoLog())
			.toHashCode();
	}
} 