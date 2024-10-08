package com.mercurio.lms.edi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

/**
 * NotaFiscalEdiComplemento generated by hbm2java
 */
@Entity
@Table(name = "LOG_ARQUIVO_EDI_DETALHE_COMPL")
@Proxy(lazy=false)  
public class LogEDIComplemento implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long idComplemento;
	private LogEDIDetalhe logEDIDetalhe;
	private String valorComplemento;
	private String nomeComplemento;
	private YearMonthDay dtLog;
	
	public LogEDIComplemento() {
	}
	
	@Id
	@Column(name = "ID_NOTA_FISCAL_EDI_COMPL", nullable = false)
	public Long getIdComplemento() {
		return this.idComplemento;
	}

	public void setIdComplemento(Long idComplemento) {
		this.idComplemento = idComplemento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOADID_LOG_ARQUIVO_EDI_DETALHE", nullable = false)
	public LogEDIDetalhe getLogEDIDetalhe() {
		return logEDIDetalhe;
	}

	public void setLogEDIDetalhe(LogEDIDetalhe logEDIDetalhe) {
		this.logEDIDetalhe = logEDIDetalhe;
	}

	@Column(name = "VALOR_COMPLEMENTO",  length = 60)
	public String getValorComplemento() {
		return this.valorComplemento;
	}

	public void setValorComplemento(String valorComplemento) {
		this.valorComplemento = valorComplemento;
	}

	@Column(name = "NOME_COMPLEMENTO",  length = 40)
	public String getNomeComplemento() {
		return nomeComplemento;
	}

	public void setNomeComplemento(String nomeComplemento) {
		this.nomeComplemento = nomeComplemento;
	}

	@Column(name = "DH_LOG")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	public YearMonthDay getDtLog() {
		return dtLog;
	}

	public void setDtLog(YearMonthDay dtLog) {
		this.dtLog = dtLog;
	}

}
