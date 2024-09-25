package com.mercurio.lms.vendas.model;

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
@Table(name = "ORIGEM_PROPOSTA_FOB")
@SequenceGenerator(name = "ORIGEM_PROPOSTA_FOB_SEQ", sequenceName = "ORIGEM_PROPOSTA_FOB_SQ")
public class OrigemPropostaFOB implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORIGEM_PROPOSTA_FOB_SEQ")
	@Column(name = "ID_ORIGEM_PROPOSTA_FOB", nullable = false)	
	private Long idOrigemPropostaFOB;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PROPOSTA_FOB",nullable=false)	
	private PropostaFOB propostaFOB;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL",nullable=false)
	private Filial filial;
	
	public Long getIdOrigemPropostaFOB() {
		return idOrigemPropostaFOB;
	}

	public void setIdOrigemPropostaFOB(Long idOrigemPropostaFOB) {
		this.idOrigemPropostaFOB = idOrigemPropostaFOB;
	}

	public PropostaFOB getPropostaFOB() {
		return propostaFOB;
	}

	public void setPropostaFOB(PropostaFOB propostaFOB) {
		this.propostaFOB = propostaFOB;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}	
}
