package com.mercurio.lms.tributos.model;

import java.io.Serializable;

public class EmbasamentoLegalIcms implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long   idEmbasamento;
	
	private String dsEmbLegalResumido;
	
	private String dsEmbLegalCompleto;
	
	private String cdEmbLegalMasterSaf;
	
	private String obEmbLegalIcms;
	
	private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaOrigem;

    private com.mercurio.lms.tributos.model.TipoTributacaoIcms tipoTributacaoIcms;

	public Long getIdEmbasamento() {
		return idEmbasamento;
	}

	public void setIdEmbasamento(Long idEmbasamento) {
		this.idEmbasamento = idEmbasamento;
	}

	public String getDsEmbLegalResumido() {
		return dsEmbLegalResumido;
	}

	public void setDsEmbLegalResumido(String dsEmbLegalResumido) {
		this.dsEmbLegalResumido = dsEmbLegalResumido;
	}

	public String getDsEmbLegalCompleto() {
		return dsEmbLegalCompleto;
	}

	public void setDsEmbLegalCompleto(String dsEmbLegalCompleto) {
		this.dsEmbLegalCompleto = dsEmbLegalCompleto;
	}

	public String getCdEmbLegalMasterSaf() {
		return cdEmbLegalMasterSaf;
	}

	public void setCdEmbLegalMasterSaf(String cdEmbLegalMasterSaf) {
		this.cdEmbLegalMasterSaf = cdEmbLegalMasterSaf;
	}

	public String getObEmbLegalIcms() {
		return obEmbLegalIcms;
	}

	public void setObEmbLegalIcms(String obEmbLegalIcms) {
		this.obEmbLegalIcms = obEmbLegalIcms;
	}

	public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaOrigem() {
		return unidadeFederativaOrigem;
	}

	public void setUnidadeFederativaOrigem(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaOrigem) {
		this.unidadeFederativaOrigem = unidadeFederativaOrigem;
	}

	public com.mercurio.lms.tributos.model.TipoTributacaoIcms getTipoTributacaoIcms() {
		return tipoTributacaoIcms;
	}

	public void setTipoTributacaoIcms(
			com.mercurio.lms.tributos.model.TipoTributacaoIcms tipoTributacaoIcms) {
		this.tipoTributacaoIcms = tipoTributacaoIcms;
	}
    
}
