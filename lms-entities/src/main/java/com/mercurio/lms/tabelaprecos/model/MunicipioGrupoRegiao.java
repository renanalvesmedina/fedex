package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;

import com.mercurio.lms.municipios.model.Municipio;

@Entity
@Table(name = "MUNICIPIO_GRUPO_REGIAO")
@SequenceGenerator(name = "MUNICIPIO_GRUPO_REGIAO_SEQ", sequenceName = "MUNICIPIO_GRUPO_REGIAO_SQ", allocationSize = 1)
public class MunicipioGrupoRegiao implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idMunicipioGrupoRegiao;
	private GrupoRegiao grupoRegiao;
	private Municipio municipio;

	public MunicipioGrupoRegiao clone(GrupoRegiao newGrupoRegiao)
			throws Throwable {
		MunicipioGrupoRegiao newMunicipioGrupoRegiao = (MunicipioGrupoRegiao) BeanUtils
				.cloneBean(this);
		newMunicipioGrupoRegiao.setIdMunicipioGrupoRegiao(null);
		newMunicipioGrupoRegiao.setGrupoRegiao(newGrupoRegiao);
		
		return newMunicipioGrupoRegiao;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MUNICIPIO_GRUPO_REGIAO_SEQ")
	@Column(name = "ID_MUNICIPIO_GRUPO_REGIAO", nullable = false)
	public Long getIdMunicipioGrupoRegiao() {
		return idMunicipioGrupoRegiao;
	}

	public void setIdMunicipioGrupoRegiao(Long idMunicipioGrupoRegiao) {
		this.idMunicipioGrupoRegiao = idMunicipioGrupoRegiao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO_REGIAO", nullable = false)
	public GrupoRegiao getGrupoRegiao() {
		return grupoRegiao;
	}

	public void setGrupoRegiao(GrupoRegiao grupoRegiao) {
		this.grupoRegiao = grupoRegiao;
	}

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO", nullable = false, unique = true)
	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

}
