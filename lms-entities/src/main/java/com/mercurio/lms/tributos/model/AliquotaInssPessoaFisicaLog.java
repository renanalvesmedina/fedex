package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class AliquotaInssPessoaFisicaLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idAliquotaInssPessoaFisicaLog;
	private AliquotaInssPessoaFisica aliquotaInssPessoaFisica;
	private YearMonthDay dtInicioVigencia;
	private BigDecimal pcAliquota;
	private BigDecimal pcBcalcReduzida;
	private BigDecimal vlSalarioBase;
	private BigDecimal vlMaximoRecolhimento;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdAliquotaInssPessoaFisicaLog() {
   
		return idAliquotaInssPessoaFisicaLog;
	}
   
	public void setIdAliquotaInssPessoaFisicaLog(
			long idAliquotaInssPessoaFisicaLog) {
   
		this.idAliquotaInssPessoaFisicaLog = idAliquotaInssPessoaFisicaLog;
	}
	
	public AliquotaInssPessoaFisica getAliquotaInssPessoaFisica() {
   
		return aliquotaInssPessoaFisica;
	}
   
	public void setAliquotaInssPessoaFisica(
			AliquotaInssPessoaFisica aliquotaInssPessoaFisica) {
   
		this.aliquotaInssPessoaFisica = aliquotaInssPessoaFisica;
	}
	
	public YearMonthDay getDtInicioVigencia() {
   
		return dtInicioVigencia;
	}
   
	public void setDtInicioVigencia(YearMonthDay dtInicioVigencia) {
   
		this.dtInicioVigencia = dtInicioVigencia;
	}
	
	public BigDecimal getPcAliquota() {
   
		return pcAliquota;
	}
   
	public void setPcAliquota(BigDecimal pcAliquota) {
   
		this.pcAliquota = pcAliquota;
	}
	
	public BigDecimal getPcBcalcReduzida() {
   
		return pcBcalcReduzida;
	}
   
	public void setPcBcalcReduzida(BigDecimal pcBcalcReduzida) {
   
		this.pcBcalcReduzida = pcBcalcReduzida;
	}
	
	public BigDecimal getVlSalarioBase() {
   
		return vlSalarioBase;
	}
   
	public void setVlSalarioBase(BigDecimal vlSalarioBase) {
   
		this.vlSalarioBase = vlSalarioBase;
	}
	
	public BigDecimal getVlMaximoRecolhimento() {
   
		return vlMaximoRecolhimento;
	}
   
	public void setVlMaximoRecolhimento(BigDecimal vlMaximoRecolhimento) {
   
		this.vlMaximoRecolhimento = vlMaximoRecolhimento;
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
		return new ToStringBuilder(this).append(
				"idAliquotaInssPessoaFisicaLog",
				getIdAliquotaInssPessoaFisicaLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaInssPessoaFisicaLog))
			return false;
		AliquotaInssPessoaFisicaLog castOther = (AliquotaInssPessoaFisicaLog) other;
		return new EqualsBuilder().append(
				this.getIdAliquotaInssPessoaFisicaLog(),
				castOther.getIdAliquotaInssPessoaFisicaLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaInssPessoaFisicaLog())
			.toHashCode();
	}
} 