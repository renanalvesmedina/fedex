package com.mercurio.lms.franqueados.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "LOG_BATCH_FRQ")
@SequenceGenerator(name = "LOG_BATCH_FRQ_SEQ", sequenceName = "LOG_BATCH_FRQ_SQ", allocationSize = 1)
public class LogBatchFranqueado implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LABEL_FRANQUIA = "FRANQUIA";
	
	private long idLogBatchFrq;
	private String dsProcesso;
	private DateTime dhLog;
	private String dsLog;
	private char blErro = 'N';
	
	public LogBatchFranqueado() {
	}
	
	public LogBatchFranqueado(long idLogBatchFrq, String dsProcesso,
			DateTime dhLog, String dsLog, char blErro) {
		this.idLogBatchFrq = idLogBatchFrq;
		this.dsProcesso = dsProcesso;
		this.dhLog = dhLog;
		this.dsLog = dsLog;
		this.blErro = blErro;
	}
	
	public LogBatchFranqueado(String dsProcesso, DateTime dhLog,
			String dsLog, char blErro) {
		this.dsProcesso = dsProcesso;
		this.dhLog = dhLog;
		this.dsLog = dsLog;
		this.blErro = blErro;
	}

	public LogBatchFranqueado(String tpProcesso, String dsLog) {
		this.dhLog = new DateTime();
		this.dsProcesso = tpProcesso;
		this.dsLog = dsLog;
	}

	public LogBatchFranqueado(Long idFranquia, String tpProcesso, String dsLog) {
		this.dhLog = new DateTime();
		this.dsProcesso = tpProcesso;
		
		StringBuilder dsLogCompact = new StringBuilder();
		dsLogCompact.append(LABEL_FRANQUIA).append(": ").append(idFranquia).append(" - ").append(dsLog);
		
		this.dsLog = dsLogCompact.toString();
	}

	@Id
	@Column(name = "ID_LOG_BATCH_FRQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_BATCH_FRQ_SEQ")
	public long getIdLogBatchFrq() {
		return this.idLogBatchFrq;
	}

	public void setIdLogBatchFrq(long idLogBatchFrq) {
		this.idLogBatchFrq = idLogBatchFrq;
	}

	@Column(name = "DS_PROCESSO", nullable = false)
	public String getDsProcesso() {
		return this.dsProcesso;
	}

	public void setDsProcesso(String dsProcesso) {
		this.dsProcesso = dsProcesso;
	}
	
	@Column(name = "DH_LOG", nullable = false, length = 7)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType")
	public DateTime getDhLog() {
		return this.dhLog;
	}

	public void setDhLog(DateTime dhLog) {
		this.dhLog = dhLog;
	}
	
	@Column(name = "DS_LOG", nullable = false)
	public String getDsLog() {
		return this.dsLog;
	}

	public void setDsLog(String dsLog) {
		this.dsLog = dsLog;
	}
	
	@Column(name = "BL_ERRO", nullable = false, length = 1)
	public char getBlErro() {
		return this.blErro;
	}

	public void setBlErro(char blErro) {
		this.blErro = blErro;
	}

}
