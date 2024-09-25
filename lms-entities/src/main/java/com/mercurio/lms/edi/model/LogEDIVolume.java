package com.mercurio.lms.edi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * NotaFiscalEdiVolume generated by hbm2java
 */
@Entity
@Table(name = "LOG_ARQUIVO_EDI_DETALHE_VOLUME")
@Proxy(lazy=false)  
public class LogEDIVolume implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long idVolume;
	private LogEDIDetalhe logEDIDetalhe;
	private String codigoVolume;
	private String cdBarraPostoAvancado;

	@Id
	@Column(name = "ID_LOG_ARQUIVO_EDI_DETALHE_VOL", nullable = false, precision = 10, scale = 0)
	public Long getIdVolume() {
		return idVolume;
	}

	public void setIdVolume(Long idVolume) {
		this.idVolume = idVolume;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LOADID_LOG_ARQUIVO_EDI_DETALHE", nullable = false)
	public LogEDIDetalhe getLogEDIDetalhe() {
		return logEDIDetalhe;
	}

	public void setLogEDIDetalhe(LogEDIDetalhe logEDIDetalhe) {
		this.logEDIDetalhe = logEDIDetalhe;
	}

	@Column(name = "CODIGO_VOLUME", length = 60)
	public String getCodigoVolume() {
		return this.codigoVolume;
	}

	public void setCodigoVolume(String codigoVolume) {
		this.codigoVolume = codigoVolume;
	}

	@Column(name = "CD_BARRA_POSTO_AVANCADO", length = 60)
	public String getCdBarraPostoAvancado() {
		return cdBarraPostoAvancado;
	}

	public void setCdBarraPostoAvancado(String cdBarraPostoAvancado) {
		this.cdBarraPostoAvancado = cdBarraPostoAvancado;
	}	
}
