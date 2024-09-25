package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class InscricaoEstadualLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idInscricaoEstadualLog;
	private InscricaoEstadual inscricaoEstadual;
	private Pessoa pessoa;
	private UnidadeFederativa unidadeFederativa;
	private String nrInscricaoEstadual;
	private boolean blIndicadorPadrao;
	private DomainValue tpSituacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdInscricaoEstadualLog() {
   
		return idInscricaoEstadualLog;
	}
   
	public void setIdInscricaoEstadualLog(long idInscricaoEstadualLog) {
   
		this.idInscricaoEstadualLog = idInscricaoEstadualLog;
	}
	
	public InscricaoEstadual getInscricaoEstadual() {
   
		return inscricaoEstadual;
	}
   
	public void setInscricaoEstadual(InscricaoEstadual inscricaoEstadual) {
   
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	public Pessoa getPessoa() {
   
		return pessoa;
	}
   
	public void setPessoa(Pessoa pessoa) {
   
		this.pessoa = pessoa;
	}
	
	public UnidadeFederativa getUnidadeFederativa() {
   
		return unidadeFederativa;
	}
   
	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
   
		this.unidadeFederativa = unidadeFederativa;
	}
	
	public String getNrInscricaoEstadual() {
   
		return nrInscricaoEstadual;
	}
   
	public void setNrInscricaoEstadual(String nrInscricaoEstadual) {
   
		this.nrInscricaoEstadual = nrInscricaoEstadual;
	}
	
	public boolean isBlIndicadorPadrao() {
   
		return blIndicadorPadrao;
	}
   
	public void setBlIndicadorPadrao(boolean blIndicadorPadrao) {
   
		this.blIndicadorPadrao = blIndicadorPadrao;
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
		return new ToStringBuilder(this).append("idInscricaoEstadualLog",
				getIdInscricaoEstadualLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof InscricaoEstadualLog))
			return false;
		InscricaoEstadualLog castOther = (InscricaoEstadualLog) other;
		return new EqualsBuilder().append(this.getIdInscricaoEstadualLog(),
				castOther.getIdInscricaoEstadualLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdInscricaoEstadualLog())
			.toHashCode();
	}
} 