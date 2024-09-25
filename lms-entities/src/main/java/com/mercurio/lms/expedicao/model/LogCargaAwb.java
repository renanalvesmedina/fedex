package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mercurio.lms.municipios.model.CiaFilialMercurio;

/**
 * @author JonasFE
 *
 */
@Entity
@Table(name="LOG_CARGA_AWB")
@SequenceGenerator(name = "LOG_CARGA_AWB_SQ", sequenceName = "LOG_CARGA_AWB_SQ", allocationSize=1)
public class LogCargaAwb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_LOG_CARGA_AWB", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_CARGA_AWB_SQ")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CIA_FILIAL_MERCURIO")
	private CiaFilialMercurio ciaFilialMercurio;
	
	@Column(name="DS_SERIE", length=30, nullable=true)
	private String dsSerie;
	
	@Column(name="NR_AWB", length=30, nullable=true)
	private Long nrAwb;
	
	@Column(name="DS_MENSAGEM", length=5, nullable=false)
	private String dsMensagem;
	
	@Column(name="NR_CHAVE", length=5, nullable=false)
	private String nrChave;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DH_INCLUSAO", length = 7)
	private Date dhInclusao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CiaFilialMercurio getCiaFilialMercurio() {
		return ciaFilialMercurio;
	}

	public void setCiaFilialMercurio(CiaFilialMercurio ciaFilialMercurio) {
		this.ciaFilialMercurio = ciaFilialMercurio;
	}

	public String getDsSerie() {
		return dsSerie;
	}

	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}

	public Long getNrAwb() {
		return nrAwb;
	}

	public void setNrAwb(Long nrAwb) {
		this.nrAwb = nrAwb;
	}

	public String getDsMensagem() {
		return dsMensagem;
	}

	public void setDsMensagem(String dsMensagem) {
		this.dsMensagem = dsMensagem;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public Date getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(Date dhInclusao) {
		this.dhInclusao = dhInclusao;
	}
}
