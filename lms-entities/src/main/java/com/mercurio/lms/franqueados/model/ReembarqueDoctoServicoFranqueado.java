package com.mercurio.lms.franqueados.model;

// Generated 17/04/2014 14:27:46 by Hibernate Tools 3.4.0.CR1

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

import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.expedicao.model.DoctoServico;

/**
 * ReembarqueDocServFrq generated by hbm2java
 */
@Entity
@Table(name = "REEMBARQUE_DOC_SERV_FRQ")
@SequenceGenerator(name = "REEMBARQUE_DOC_SERV_FRQ_SEQ", sequenceName = "REEMBARQUE_DOC_SERV_FRQ_SQ", allocationSize = 1)
public class ReembarqueDoctoServicoFranqueado implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_REEMBARQUE_DOC_SERV_FRQ", unique = true, nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REEMBARQUE_DOC_SERV_FRQ_SEQ")
	private long idReembarqueDocServFrq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FRANQUIA", nullable = false)
	private Franquia franquia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico conhecimento;
	
	@Column(name = "DT_COMPETENCIA", nullable = false, length = 7)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtCompetencia;
	
	@Column(name = "PS_MERCADORIA", nullable = false, precision = 18, scale = 3)
	private BigDecimal psMercadoria;
	
	@Column(name = "VL_CTE", nullable = false, precision = 18)
	private BigDecimal vlCte;
	
	@Column(name = "VL_TONELADA", nullable = false, precision = 18)
	private BigDecimal vlTonelada;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MANIFESTO", nullable = false)
	private Manifesto manifesto;
	
	public ReembarqueDoctoServicoFranqueado() {
		super();
	}

	public long getIdReembarqueDocServFrq() {
		return this.idReembarqueDocServFrq;
	}

	public void setIdReembarqueDocServFrq(long idReembarqueDocServFrq) {
		this.idReembarqueDocServFrq = idReembarqueDocServFrq;
	}

	public Franquia getFranquia() {
		return this.franquia;
	}

	public void setFranquia(Franquia franquia) {
		this.franquia = franquia;
	}

	public DoctoServico getConhecimento() {
		return this.conhecimento;
	}

	public void setConhecimento(DoctoServico conhecimento) {
		this.conhecimento = conhecimento;
	}

	public YearMonthDay getDtCompetencia() {
		return this.dtCompetencia;
	}

	public void setDtCompetencia(YearMonthDay dtCompetencia) {
		this.dtCompetencia = dtCompetencia;
	}

	public BigDecimal getPsMercadoria() {
		return this.psMercadoria;
	}

	public void setPsMercadoria(BigDecimal psMercadoria) {
		this.psMercadoria = psMercadoria;
	}

	public BigDecimal getVlCte() {
		return this.vlCte;
	}

	public void setVlCte(BigDecimal vlCte) {
		this.vlCte = vlCte;
	}

	public BigDecimal getVlTonelada() {
		return this.vlTonelada;
	}

	public void setVlTonelada(BigDecimal vlTonelada) {
		this.vlTonelada = vlTonelada;
	}

	public Manifesto getManifesto() {
		return manifesto;
	}

	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	
}
