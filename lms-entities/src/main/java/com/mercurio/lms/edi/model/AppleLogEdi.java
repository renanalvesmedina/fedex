package com.mercurio.lms.edi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "APPLE_LOG_EDI")
@SequenceGenerator(name = "APPLE_LOG_EDI_SEQ", sequenceName = "APPLE_LOG_EDI_SQ", allocationSize = 1)
public class AppleLogEdi implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idAppleLogEdi;
	
	private DateTime dhAppleLogEdi;
	
	private DateTime dhEventoEdi;
	
	private Long nrTransacao;
	
	private String tpAppleLogEdi;
	
	private Long nrTransacaoResp; 
	
	public void setIdAppleLogEdi(Long idAppleLogEdi) {
		this.idAppleLogEdi = idAppleLogEdi;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLE_LOG_EDI_SEQ")
	@Column(name = "ID_APPLE_LOG_EDI", nullable = false)
	public Long getIdAppleLogEdi() {
		return idAppleLogEdi;
	}

	public void setDhAppleLogEdi(DateTime dhAppleLogEdi) {
		this.dhAppleLogEdi = dhAppleLogEdi;
	}

	@Columns(columns = { @Column(name = "DH_APPLE_LOG_EDI", nullable = false),
			@Column(name = "DH_APPLE_LOG_EDI_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhAppleLogEdi() {
		return dhAppleLogEdi;
	}

	public void setDhEventoEdi(DateTime dhEventoEdi) {
		this.dhEventoEdi = dhEventoEdi;
	}

	@Columns(columns = { @Column(name = "DH_EVENTO_EDI", nullable = false),
			@Column(name = "DH_EVENTO_EDI_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhEventoEdi() {
		return dhEventoEdi;
	}

	public void setNrTransacao(Long nrTransacao) {
		this.nrTransacao = nrTransacao;
	}

	@Column(name = "NR_TRANSACAO", nullable = false)
	public Long getNrTransacao() {
		return nrTransacao;
	}

	public void setTpAppleLogEdi(String tpAppleLogEdi) {
		this.tpAppleLogEdi = tpAppleLogEdi;
	}

	@Column(name = "TP_APPLE_LOG_EDI ", nullable = false)
	public String getTpAppleLogEdi() {
		return tpAppleLogEdi;
	}

	public void setNrTransacaoResp(Long nrTransacaoResp) {
		this.nrTransacaoResp = nrTransacaoResp;
	}

	@Column(name = "NR_TRANSACAO_RESP", nullable = true)
	public Long getNrTransacaoResp() {
		return nrTransacaoResp;
	}
}
