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
import com.mercurio.lms.municipios.model.Municipio;

/**
 * @author JonasFE
 *
 */
@Entity
@Table(name="CODIGO_MUNICIPIO_FILIAL")
@SequenceGenerator(name = "CODIGO_MUNICIPIO_FILIAL_SQ", sequenceName = "CODIGO_MUNICIPIO_FILIAL_SQ", allocationSize=1)
public class CodigoMunicipioFilial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_CODIGO_MUNICIPIO_FILIAL", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CODIGO_MUNICIPIO_FILIAL_SQ")
	private Long idCodigoMunicipioFilial;
	
	@Column(name="DS_CODIGO", length=20, nullable = false)
	private String dsCodigo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	private Filial filial;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO", nullable = false)
	private Municipio municipio;

	public Long getIdCodigoMunicipioFilial() {
		return idCodigoMunicipioFilial;
	}

	public void setIdCodigoMunicipioFilial(Long idCodigoMunicipioFilial) {
		this.idCodigoMunicipioFilial = idCodigoMunicipioFilial;
	}

	public String getDsCodigo() {
		return dsCodigo;
	}

	public void setDsCodigo(String dsCodigo) {
		this.dsCodigo = dsCodigo;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
}