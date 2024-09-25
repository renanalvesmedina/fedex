package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class ObservacaoICMSLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idObservacaoICMSLog;
	private ObservacaoICMS observacaoIcms;
	private DescricaoTributacaoIcms descricaoTributacaoIcms;
	private Long nrOrdemImpressao;
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpObservacaoIcms;
	private String obObservacaoIcms;
	private YearMonthDay dtVigenciaFinal;
	private Long nrVersao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdObservacaoICMSLog() {
   
		return idObservacaoICMSLog;
	}
   
	public void setIdObservacaoICMSLog(long idObservacaoICMSLog) {
   
		this.idObservacaoICMSLog = idObservacaoICMSLog;
	}
	
	public ObservacaoICMS getObservacaoIcms() {
   
		return observacaoIcms;
	}
   
	public void setObservacaoIcms(ObservacaoICMS observacaoIcms) {
   
		this.observacaoIcms = observacaoIcms;
	}
	
	public DescricaoTributacaoIcms getDescricaoTributacaoIcms() {
   
		return descricaoTributacaoIcms;
	}
   
	public void setDescricaoTributacaoIcms(
			DescricaoTributacaoIcms descricaoTributacaoIcms) {
   
		this.descricaoTributacaoIcms = descricaoTributacaoIcms;
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
	
	public DomainValue getTpObservacaoIcms() {
   
		return tpObservacaoIcms;
	}
   
	public void setTpObservacaoIcms(DomainValue tpObservacaoIcms) {
   
		this.tpObservacaoIcms = tpObservacaoIcms;
	}
	
	public String getObObservacaoIcms() {
   
		return obObservacaoIcms;
	}
   
	public void setObObservacaoIcms(String obObservacaoIcms) {
   
		this.obObservacaoIcms = obObservacaoIcms;
	}
	
	public YearMonthDay getDtVigenciaFinal() {
   
		return dtVigenciaFinal;
	}
   
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
   
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public Long getNrVersao() {
   
		return nrVersao;
	}
   
	public void setNrVersao(Long nrVersao) {
   
		this.nrVersao = nrVersao;
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
		return new ToStringBuilder(this).append("idObservacaoICMSLog",
				getIdObservacaoICMSLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ObservacaoICMSLog))
			return false;
		ObservacaoICMSLog castOther = (ObservacaoICMSLog) other;
		return new EqualsBuilder().append(this.getIdObservacaoICMSLog(),
				castOther.getIdObservacaoICMSLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdObservacaoICMSLog())
			.toHashCode();
	}
} 