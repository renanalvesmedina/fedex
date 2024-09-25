package com.mercurio.lms.expedicao.model;

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
import com.mercurio.lms.municipios.model.UnidadeFederativa;

@Entity
@Table(name="FILIAL_PERCURSO_UF")
@SequenceGenerator(name="FILIAL_PERCURSO_UF_SQ", sequenceName="FILIAL_PERCURSO_UF_SQ", allocationSize=1)
public class FilialPercursoUF implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FILIAL_PERCURSO_UF_SQ")
	@Column(name="ID_FILIAL_PERCURSO_UF", nullable=false)
	private Long idFilialPercursoUF;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_ORIGEM", nullable = false)
	private Filial filialOrigem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_DESTINO", nullable = false)
	private Filial filialDestino;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_UNIDADE_FEDERATIVA", nullable = false)
	private UnidadeFederativa unidadeFederativa;
	
	@Column(name="NR_ORDEM", length=10, nullable = false)
	private Long nrOrdem;

	public Long getIdFilialPercursoUF() {
		return idFilialPercursoUF;
	}

	public void setIdFilialPercursoUF(Long idFilialPercursoUF) {
		this.idFilialPercursoUF = idFilialPercursoUF;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public Long getNrOrdem() {
		return nrOrdem;
	}

	public void setNrOrdem(Long nrOrdem) {
		this.nrOrdem = nrOrdem;
	}
	
}
