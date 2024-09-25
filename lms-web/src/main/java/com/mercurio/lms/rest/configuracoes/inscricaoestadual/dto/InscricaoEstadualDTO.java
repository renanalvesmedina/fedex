package com.mercurio.lms.rest.configuracoes.inscricaoestadual.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class InscricaoEstadualDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idInscricaoEstadual;

	private String sgUnidadeFederativa;
	private String nrInscricaoEstadual;
	private Boolean blIndicadorPadrao;
	private DomainValue situacao;

	public Long getIdInscricaoEstadual() {
		return idInscricaoEstadual;
	}

	public void setIdInscricaoEstadual(Long idInscricaoEstadual) {
		this.idInscricaoEstadual = idInscricaoEstadual;
	}

	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}

	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}

	public String getNrInscricaoEstadual() {
		return nrInscricaoEstadual;
	}

	public void setNrInscricaoEstadual(String nrInscricaoEstadual) {
		this.nrInscricaoEstadual = nrInscricaoEstadual;
	}

	public Boolean getBlIndicadorPadrao() {
		return blIndicadorPadrao;
	}

	public void setBlIndicadorPadrao(Boolean blIndicadorPadrao) {
		this.blIndicadorPadrao = blIndicadorPadrao;
	}

	public DomainValue getSituacao() {
		return situacao;
	}

	public void setSituacao(DomainValue situacao) {
		this.situacao = situacao;
	}

}
