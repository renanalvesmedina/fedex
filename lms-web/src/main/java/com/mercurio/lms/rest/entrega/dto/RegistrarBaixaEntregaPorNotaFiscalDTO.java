package com.mercurio.lms.rest.entrega.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class RegistrarBaixaEntregaPorNotaFiscalDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private Long idDoctoServico;
	private String doctoServico;
	private Long idManifesto;
	private String tpManifesto;
	private DateTime dhOcorrencia;
	private Short cdOcorrenciaEntrega;
	
	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public String getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(String doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Long getIdManifesto() {
		return idManifesto;
	}

	public void setIdManifesto(Long idManifesto) {
		this.idManifesto = idManifesto;
	}

	public String getTpManifesto() {
		return tpManifesto;
	}

	public void setTpManifesto(String tpManifesto) {
		this.tpManifesto = tpManifesto;
	}

	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

	public Short getCdOcorrenciaEntrega() {
		return cdOcorrenciaEntrega;
	}

	public void setCdOcorrenciaEntrega(Short cdOcorrenciaEntrega) {
		this.cdOcorrenciaEntrega = cdOcorrenciaEntrega;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}
	
}
