package com.mercurio.lms.ppd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PPD_LOTES_RECIBO")
@SequenceGenerator(name = "LOTE_RECIBO_SEQ", sequenceName = "PPD_LOTES_RECIBO_SQ")
public class PpdLoteRecibo implements Serializable  {	
	private static final long serialVersionUID = 1L;
	private Long idLoteRecibo;
	private PpdRecibo recibo;
	private PpdLoteJde lote;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOTE_RECIBO_SEQ")
	@Column(name = "ID_LOTE_RECIBO", nullable = false)
	public Long getIdLoteRecibo() {
		return idLoteRecibo;
	}

	public void setIdLoteRecibo(Long idLoteRecibo) {
		this.idLoteRecibo = idLoteRecibo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_RECIBO", nullable = false)
	public PpdRecibo getRecibo() {
		return recibo;
	}

	public void setRecibo(PpdRecibo recibo) {
		this.recibo = recibo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_LOTE_JDE", nullable = false)
	public PpdLoteJde getLote() {
		return lote;
	}

	public void setLote(PpdLoteJde lote) {
		this.lote = lote;
	}	
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idLoteRecibo", this.getIdLoteRecibo());
		bean.putAll(this.getLote().getMapped());
		bean.putAll(this.getRecibo().getMapped());
		return bean;
	}
}
