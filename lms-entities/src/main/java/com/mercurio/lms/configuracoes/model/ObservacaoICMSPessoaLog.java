package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class ObservacaoICMSPessoaLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idObservacaoICMSPessoaLog;
	private ObservacaoICMSPessoa observacaoIcmsPessoa;
	private InscricaoEstadual inscricaoEstadual;
	private Long nrOrdemImpressao;
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpObservacaoIcmsPessoa;
	private YearMonthDay dtVigenciaFinal;
	private String obObservacaoIcmsPessoa;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdObservacaoICMSPessoaLog() {
   
		return idObservacaoICMSPessoaLog;
	}
   
	public void setIdObservacaoICMSPessoaLog(long idObservacaoICMSPessoaLog) {
   
		this.idObservacaoICMSPessoaLog = idObservacaoICMSPessoaLog;
	}
	
	public ObservacaoICMSPessoa getObservacaoIcmsPessoa() {
   
		return observacaoIcmsPessoa;
	}
   
	public void setObservacaoIcmsPessoa(
			ObservacaoICMSPessoa observacaoIcmsPessoa) {
   
		this.observacaoIcmsPessoa = observacaoIcmsPessoa;
	}
	
	public InscricaoEstadual getInscricaoEstadual() {
   
		return inscricaoEstadual;
	}
   
	public void setInscricaoEstadual(InscricaoEstadual inscricaoEstadual) {
   
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	public Long getNrOrdemImpressao() {
   
		return nrOrdemImpressao;
	}
   
	public void setNrOrdemImpressao(Long nrOrdemImpressao) {
   
		this.nrOrdemImpressao = nrOrdemImpressao;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public DomainValue getTpObservacaoIcmsPessoa() {
   
		return tpObservacaoIcmsPessoa;
	}
   
	public void setTpObservacaoIcmsPessoa(DomainValue tpObservacaoIcmsPessoa) {
   
		this.tpObservacaoIcmsPessoa = tpObservacaoIcmsPessoa;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public String getObObservacaoIcmsPessoa() {
   
		return obObservacaoIcmsPessoa;
	}
   
	public void setObObservacaoIcmsPessoa(String obObservacaoIcmsPessoa) {
   
		this.obObservacaoIcmsPessoa = obObservacaoIcmsPessoa;
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
		return new ToStringBuilder(this).append("idObservacaoICMSPessoaLog",
				getIdObservacaoICMSPessoaLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ObservacaoICMSPessoaLog))
			return false;
		ObservacaoICMSPessoaLog castOther = (ObservacaoICMSPessoaLog) other;
		return new EqualsBuilder().append(this.getIdObservacaoICMSPessoaLog(),
				castOther.getIdObservacaoICMSPessoaLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdObservacaoICMSPessoaLog())
			.toHashCode();
	}
} 