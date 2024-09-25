package com.mercurio.lms.rest.entrega.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class RegistrarBaixaEntregaPorNotaFiscalListDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private Long idDoctoServico;
	private String doctoServico;
	private Long idManifesto;
	private String tpManifesto;
	private String manifestoEntrega;
	private String manifestoViagem;
	private String ocorrenciaEntrega;
	private String dhOcorrencia;
	private String nmUsuario;
	
	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public String getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(String doctoServico) {
		this.doctoServico = doctoServico;
	}

	public String getManifestoEntrega() {
		return manifestoEntrega;
	}

	public void setManifestoEntrega(String manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}

	public String getManifestoViagem() {
		return manifestoViagem;
	}

	public void setManifestoViagem(String manifestoViagem) {
		this.manifestoViagem = manifestoViagem;
	}

	public String getOcorrenciaEntrega() {
		return ocorrenciaEntrega;
	}

	public void setOcorrenciaEntrega(String ocorrenciaEntrega) {
		this.ocorrenciaEntrega = ocorrenciaEntrega;
	}

	public String getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(String dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
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
	
	
}
