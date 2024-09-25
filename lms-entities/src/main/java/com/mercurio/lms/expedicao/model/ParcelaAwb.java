package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

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

@Entity
@Table(name = "PARCELA_AWB")
@SequenceGenerator(name = "PARCELA_AWB_SQ", sequenceName = "PARCELA_AWB_SQ")
public class ParcelaAwb implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARCELA_AWB_SQ")
	@Column(name="ID_PARCELA_AWB", nullable=false)
	private Long idParcelaAwb;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AWB", nullable = false)
	private Awb awb;
	
	@Column(name="DS_PARCELA_AWB", length=50)
	private String dsParcelaAwb;
	
	@Column(name="VL_PARCELA_AWB")
	private BigDecimal vlParcelaAwb;

	public Long getIdParcelaAwb() {
		return idParcelaAwb;
	}

	public void setIdParcelaAwb(Long idParcelaAwb) {
		this.idParcelaAwb = idParcelaAwb;
	}

	public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public String getDsParcelaAwb() {
		return dsParcelaAwb;
	}

	public void setDsParcelaAwb(String dsParcelaAwb) {
		this.dsParcelaAwb = dsParcelaAwb;
	}

	public BigDecimal getVlParcelaAwb() {
		return vlParcelaAwb;
	}

	public void setVlParcelaAwb(BigDecimal vlParcelaAwb) {
		this.vlParcelaAwb = vlParcelaAwb;
	}

}
