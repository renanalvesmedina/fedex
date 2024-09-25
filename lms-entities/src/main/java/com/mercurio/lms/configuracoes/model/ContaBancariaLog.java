package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class ContaBancariaLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idContaBancariaLog;
	private ContaBancaria contaBancaria;
	private Pessoa pessoa;
	private AgenciaBancaria agenciaBancaria;
	private Long nrContaBancaria;
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpConta;
	private String dvContaBancaria;
	private YearMonthDay dtVigenciaFinal;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdContaBancariaLog() {
   
		return idContaBancariaLog;
	}
   
	public void setIdContaBancariaLog(long idContaBancariaLog) {
   
		this.idContaBancariaLog = idContaBancariaLog;
	}
	
	public ContaBancaria getContaBancaria() {
   
		return contaBancaria;
	}
   
	public void setContaBancaria(ContaBancaria contaBancaria) {
   
		this.contaBancaria = contaBancaria;
	}
	
	public Pessoa getPessoa() {
   
		return pessoa;
	}
   
	public void setPessoa(Pessoa pessoa) {
   
		this.pessoa = pessoa;
	}
	
	public AgenciaBancaria getAgenciaBancaria() {
   
		return agenciaBancaria;
	}
   
	public void setAgenciaBancaria(AgenciaBancaria agenciaBancaria) {
   
		this.agenciaBancaria = agenciaBancaria;
	}
	
	public Long getNrContaBancaria() {
   
		return nrContaBancaria;
	}
   
	public void setNrContaBancaria(Long nrContaBancaria) {
   
		this.nrContaBancaria = nrContaBancaria;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public DomainValue getTpConta() {
   
		return tpConta;
	}
   
	public void setTpConta(DomainValue tpConta) {
   
		this.tpConta = tpConta;
	}
	
	public String getDvContaBancaria() {
   
		return dvContaBancaria;
	}
   
	public void setDvContaBancaria(String dvContaBancaria) {
   
		this.dvContaBancaria = dvContaBancaria;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
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
		return new ToStringBuilder(this).append("idContaBancariaLog",
				getIdContaBancariaLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ContaBancariaLog))
			return false;
		ContaBancariaLog castOther = (ContaBancariaLog) other;
		return new EqualsBuilder().append(this.getIdContaBancariaLog(),
				castOther.getIdContaBancariaLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdContaBancariaLog())
			.toHashCode();
	}
} 