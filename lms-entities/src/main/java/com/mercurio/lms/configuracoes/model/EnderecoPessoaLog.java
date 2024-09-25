package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class EnderecoPessoaLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idEnderecoPessoaLog;
	private EnderecoPessoa enderecoPessoa;
	private Pessoa pessoa;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdEnderecoPessoaLog() {
   
		return idEnderecoPessoaLog;
	}
   
	public void setIdEnderecoPessoaLog(long idEnderecoPessoaLog) {
   
		this.idEnderecoPessoaLog = idEnderecoPessoaLog;
	}
	
	public EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}

	public Pessoa getPessoa() {
   
		return pessoa;
	}
   
	public void setPessoa(Pessoa pessoa) {
   
		this.pessoa = pessoa;
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
		return new ToStringBuilder(this).append("idEnderecoPessoaLog",
				getIdEnderecoPessoaLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EnderecoPessoaLog))
			return false;
		EnderecoPessoaLog castOther = (EnderecoPessoaLog) other;
		return new EqualsBuilder().append(this.getIdEnderecoPessoaLog(),
				castOther.getIdEnderecoPessoaLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdEnderecoPessoaLog())
			.toHashCode();
	}
} 