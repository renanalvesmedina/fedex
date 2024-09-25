package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.DomainValue;

public class CptTipoValor implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long idCptTipoValor;
	
	private String dsTipoValor;
	
	private DomainValue tpGrupoValor;
	
	private DomainValue tpSituacao;

	public Long getIdCptTipoValor() {
		return idCptTipoValor;
	}

	public void setIdCptTipoValor(Long idCptTipoValor) {
		this.idCptTipoValor = idCptTipoValor;
	}

	public String getDsTipoValor() {
		return dsTipoValor;
	}

	public void setDsTipoValor(String dsTipoValor) {
		this.dsTipoValor = dsTipoValor;
	}

	public DomainValue getTpGrupoValor() {
		return tpGrupoValor;
	}

	public void setTpGrupoValor(DomainValue tpGrupoValor) {
		this.tpGrupoValor = tpGrupoValor;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

}
