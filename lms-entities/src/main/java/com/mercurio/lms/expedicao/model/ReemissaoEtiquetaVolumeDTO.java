package com.mercurio.lms.expedicao.model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.joda.time.DateTime;

/**
 * @author JonasFE
 * 
 */
public class ReemissaoEtiquetaVolumeDTO {
	private String sgFilialEmissao;
	private String nmUsuarioEmissao;
	private String dhEmissao;
	private String dsMacEmissao;
	private String sgFilialReemissao;
	private String nmUsuarioReemissao;
	private String dhReemissao;
	private String dsMacReemissao;
	private String sgFilialDocumentoServico;
	private String documentoServico;
	private String nmCliente;
	private String codigoBarras;
	private Long idReemissaoEtiquetaVolume;
	private SimpleDateFormat data=new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public ReemissaoEtiquetaVolumeDTO(HashMap<String, Object> reg) {
		sgFilialEmissao = (String) reg.get("sgFilialEmissao");
		nmUsuarioEmissao = (String) reg.get("nmUsuarioEmissao");
		dhEmissao = reg.get("dhEmissao")!=null?data.format(((DateTime)reg.get("dhEmissao")).toDate()):null;
		dsMacEmissao = (String) reg.get("dsMacEmissao");
		sgFilialReemissao = (String) reg.get("sgFilialReemissao");
		nmUsuarioReemissao = (String)reg.get("nmUsuarioReemissao");
		dhReemissao = reg.get("dhReemissao")!=null?data.format(((DateTime)reg.get("dhReemissao")).toDate()):null;
		dsMacReemissao = (String) reg.get("dsMacReemissao");
		sgFilialDocumentoServico = (String) reg.get("sgFilialDocumentoServico");
		String codDocumentoServico="00000000"+(Long) reg.get("documentoServico");
		codDocumentoServico=codDocumentoServico.substring(codDocumentoServico.length()-8, codDocumentoServico.length());
		documentoServico = sgFilialDocumentoServico +"-"+ codDocumentoServico;
		nmCliente = (String) reg.get("nmCliente");
		codigoBarras = (String) reg.get("codigoBarras");
		idReemissaoEtiquetaVolume = (Long) reg.get("idReemissaoEtiquetaVolume");
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public String getDhEmissao() {
		return dhEmissao;
	}

	public String getDhReemissao() {
		return dhReemissao;
	}

	public String getDocumentoServico() {
		return documentoServico;
	}

	public String getDsMacEmissao() {
		return dsMacEmissao;
	}

	public String getDsMacReemissao() {
		return dsMacReemissao;
	}

	public Long getIdReemissaoEtiquetaVolume() {
		return idReemissaoEtiquetaVolume;
	}

	public String getNmCliente() {
		return nmCliente;
	}

	public String getNmUsuarioEmissao() {
		return nmUsuarioEmissao;
	}

	public String getNmUsuarioReemissao() {
		return nmUsuarioReemissao;
	}

	public String getSgFilialDocumentoServico() {
		return sgFilialDocumentoServico;
	}

	public String getSgFilialEmissao() {
		return sgFilialEmissao;
	}

	public String getSgFilialReemissao() {
		return sgFilialReemissao;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public void setDhEmissao(String dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public void setDhReemissao(String dhReemissao) {
		this.dhReemissao = dhReemissao;
	}

	public void setDocumentoServico(String documentoServico) {
		this.documentoServico = documentoServico;
	}

	public void setDsMacEmissao(String dsMacEmissao) {
		this.dsMacEmissao = dsMacEmissao;
	}

	public void setDsMacReemissao(String dsMacReemissao) {
		this.dsMacReemissao = dsMacReemissao;
	}

	public void setIdReemissaoEtiquetaVolume(Long idReemissaoEtiquetaVolume) {
		this.idReemissaoEtiquetaVolume = idReemissaoEtiquetaVolume;
	}

	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}

	public void setNmUsuarioEmissao(String nmUsuarioEmissao) {
		this.nmUsuarioEmissao = nmUsuarioEmissao;
	}

	public void setNmUsuarioReemissao(String nmUsuarioReemissao) {
		this.nmUsuarioReemissao = nmUsuarioReemissao;
	}

	public void setSgFilialDocumentoServico(String sgFilialDocumentoServico) {
		this.sgFilialDocumentoServico = sgFilialDocumentoServico;
	}

	public void setSgFilialEmissao(String sgFilialEmissao) {
		this.sgFilialEmissao = sgFilialEmissao;
	}

	public void setSgFilialReemissao(String sgFilialReemissao) {
		this.sgFilialReemissao = sgFilialReemissao;
	}
}