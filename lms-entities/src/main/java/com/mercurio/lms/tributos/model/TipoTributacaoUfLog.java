package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class TipoTributacaoUfLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTipoTributacaoUfLog;
	private TipoTributacaoUf tipoTributacaoUf;
	private UnidadeFederativa unidadeFederativa;
	private TipoTributacaoIcms tipoTributacaoIcms;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTipoTributacaoUfLog() {
   
		return idTipoTributacaoUfLog;
	}
   
	public void setIdTipoTributacaoUfLog(long idTipoTributacaoUfLog) {
   
		this.idTipoTributacaoUfLog = idTipoTributacaoUfLog;
	}
	
	public TipoTributacaoUf getTipoTributacaoUf() {
   
		return tipoTributacaoUf;
	}
   
	public void setTipoTributacaoUf(TipoTributacaoUf tipoTributacaoUf) {
   
		this.tipoTributacaoUf = tipoTributacaoUf;
	}
	
	public UnidadeFederativa getUnidadeFederativa() {
   
		return unidadeFederativa;
	}
   
	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
   
		this.unidadeFederativa = unidadeFederativa;
	}
	
	public TipoTributacaoIcms getTipoTributacaoIcms() {
   
		return tipoTributacaoIcms;
	}
   
	public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
   
		this.tipoTributacaoIcms = tipoTributacaoIcms;
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
		return new ToStringBuilder(this).append("idTipoTributacaoUfLog",
				getIdTipoTributacaoUfLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoTributacaoUfLog))
			return false;
		TipoTributacaoUfLog castOther = (TipoTributacaoUfLog) other;
		return new EqualsBuilder().append(this.getIdTipoTributacaoUfLog(),
				castOther.getIdTipoTributacaoUfLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoTributacaoUfLog())
			.toHashCode();
	}
} 