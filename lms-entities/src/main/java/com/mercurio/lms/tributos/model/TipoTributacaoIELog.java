package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;

public class TipoTributacaoIELog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTipoTributacaoIELog;
	private TipoTributacaoIE tipoTributacaoIe;
	private InscricaoEstadual inscricaoEstadual;
	private YearMonthDay dtVigenciaInicial;
	private DomainValue tpSituacaoTributaria;
	private boolean blIsencaoExportacoes;
	private boolean blAceitaSubstituicao;
	private boolean blIncentivada;
	private TipoTributacaoIcms tipoTributacaoIcms;
	private YearMonthDay dtVigenciaFinal;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTipoTributacaoIELog() {
   
		return idTipoTributacaoIELog;
	}
   
	public void setIdTipoTributacaoIELog(long idTipoTributacaoIELog) {
   
		this.idTipoTributacaoIELog = idTipoTributacaoIELog;
	}
	
	public TipoTributacaoIE getTipoTributacaoIe() {
   
		return tipoTributacaoIe;
	}
   
	public void setTipoTributacaoIe(TipoTributacaoIE tipoTributacaoIe) {
   
		this.tipoTributacaoIe = tipoTributacaoIe;
	}
	
	public InscricaoEstadual getInscricaoEstadual() {
   
		return inscricaoEstadual;
	}
   
	public void setInscricaoEstadual(InscricaoEstadual inscricaoEstadual) {
   
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
   
		return dtVigenciaInicial;
	}
   
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
   
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public DomainValue getTpSituacaoTributaria() {
   
		return tpSituacaoTributaria;
	}
   
	public void setTpSituacaoTributaria(DomainValue tpSituacaoTributaria) {
   
		this.tpSituacaoTributaria = tpSituacaoTributaria;
	}
	
	public boolean isBlIsencaoExportacoes() {
   
		return blIsencaoExportacoes;
	}
   
	public void setBlIsencaoExportacoes(boolean blIsencaoExportacoes) {
   
		this.blIsencaoExportacoes = blIsencaoExportacoes;
	}
	
	public boolean isBlAceitaSubstituicao() {
   
		return blAceitaSubstituicao;
	}
   
	public void setBlAceitaSubstituicao(boolean blAceitaSubstituicao) {
   
		this.blAceitaSubstituicao = blAceitaSubstituicao;
	}
	
	public boolean isBlIncentivada() {
   
		return blIncentivada;
	}
   
	public void setBlIncentivada(boolean blIncentivada) {
   
		this.blIncentivada = blIncentivada;
	}
	
	public TipoTributacaoIcms getTipoTributacaoIcms() {
   
		return tipoTributacaoIcms;
	}
   
	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
   
		this.tipoTributacaoIcms = tipoTributacaoIcms;
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
		return new ToStringBuilder(this).append("idTipoTributacaoIELog",
				getIdTipoTributacaoIELog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoTributacaoIELog))
			return false;
		TipoTributacaoIELog castOther = (TipoTributacaoIELog) other;
		return new EqualsBuilder().append(this.getIdTipoTributacaoIELog(),
				castOther.getIdTipoTributacaoIELog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoTributacaoIELog())
			.toHashCode();
	}
} 