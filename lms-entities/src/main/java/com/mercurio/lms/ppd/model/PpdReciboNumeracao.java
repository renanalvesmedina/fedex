package com.mercurio.lms.ppd.model;

import java.io.Serializable;

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

import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "PPD_RECIBOS_NUMERACAO")
@SequenceGenerator(name = "RECIBO_NUMERACAO_SEQ", sequenceName = "PPD_RECIBOS_NUMERACAO_SQ")
public class PpdReciboNumeracao implements Serializable {	
	private static final long serialVersionUID = 1L;	
	private Long idReciboNumeracao;
	private Long nrRecibo;
	private Filial filial;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIBO_NUMERACAO_SEQ")
	@Column(name = "ID_RECIBO_NUMERACAO", nullable = false)
	public Long getIdReciboNumeracao() {
		return idReciboNumeracao;
	}

	public void setIdReciboNumeracao(Long idReciboNumeracao) {
		this.idReciboNumeracao = idReciboNumeracao;
	}

	@Column(name="NR_RECIBO",nullable=false)
	public Long getNrRecibo() {
		return nrRecibo;
	}

	public void setNrRecibo(Long nrRecibo) {
		this.nrRecibo = nrRecibo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}	
}
