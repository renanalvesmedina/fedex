package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class TipoRegistroComplementoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idTipoRegistroComplementoLog;
	private TipoRegistroComplemento tipoRegistroComplemento;
	private String dsTipoRegistroComplemento;
	private Long nrVersao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdTipoRegistroComplementoLog() {
   
		return idTipoRegistroComplementoLog;
	}
   
	public void setIdTipoRegistroComplementoLog(
			long idTipoRegistroComplementoLog) {
   
		this.idTipoRegistroComplementoLog = idTipoRegistroComplementoLog;
	}
	
	public TipoRegistroComplemento getTipoRegistroComplemento() {
   
		return tipoRegistroComplemento;
	}
   
	public void setTipoRegistroComplemento(
			TipoRegistroComplemento tipoRegistroComplemento) {
   
		this.tipoRegistroComplemento = tipoRegistroComplemento;
	}
	
	public String getDsTipoRegistroComplemento() {
   
		return dsTipoRegistroComplemento;
	}
   
	public void setDsTipoRegistroComplemento(String dsTipoRegistroComplemento) {
   
		this.dsTipoRegistroComplemento = dsTipoRegistroComplemento;
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
		return new ToStringBuilder(this).append("idTipoRegistroComplementoLog",
				getIdTipoRegistroComplementoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoRegistroComplementoLog))
			return false;
		TipoRegistroComplementoLog castOther = (TipoRegistroComplementoLog) other;
		return new EqualsBuilder().append(
				this.getIdTipoRegistroComplementoLog(),
				castOther.getIdTipoRegistroComplementoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoRegistroComplementoLog())
			.toHashCode();
	}
} 