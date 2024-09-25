package com.mercurio.lms.fretecarreteirocoletaentrega.model;

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
@Table(name = "PROPRIETARIO_RPA")
@SequenceGenerator(name = "PROPRIETARIO_RPA_SQ", sequenceName = "PROPRIETARIO_RPA_SQ", allocationSize = 1)
public class ProprietarioRPA implements Serializable {
	private static final long serialVersionUID = 1537804889099557264L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPRIETARIO_RPA_SQ")
	@Column(name = "ID_PROPRIETARIO_RPA", nullable = false)
	private Long idProprietarioRPA;
	
	@Column(name="NR_RPA", nullable = false, length = 10)
	private Long nrRPA;
		
	@Column(name="ID_RECIBO_FRETE_CARRETEIRO", nullable = false, length = 10)
	private Long idReciboFreteCarreteiro;
	
	@Columns(columns = { @Column(name = "DH_GERACAO_RPA", nullable = true),	@Column(name = "DH_GERACAO_RPA_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhGeracao;
	
	@Column(name = "ID_PROPRIETARIO", nullable = false)
	private Long idProprietario;

	public Long getIdProprietarioRPA() {
		return idProprietarioRPA;
	}

	public void setIdProprietarioRPA(Long idProprietarioRPA) {
		this.idProprietarioRPA = idProprietarioRPA;
	}

	public Long getIdReciboFreteCarreteiro() {
		return idReciboFreteCarreteiro;
	}

	public void setIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		this.idReciboFreteCarreteiro = idReciboFreteCarreteiro;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public Long getNrRPA() {
		return nrRPA;
	}

	public void setNrRPA(Long nrRPA) {
		this.nrRPA = nrRPA;
	}

	public Long getIdProprietario() {
		return idProprietario;
	}

	public void setIdProprietario(Long idProprietario) {
		this.idProprietario = idProprietario;
	}
	
	
	
	
}