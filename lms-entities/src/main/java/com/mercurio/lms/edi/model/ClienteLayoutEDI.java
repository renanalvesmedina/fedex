package com.mercurio.lms.edi.model;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.DomainValue;

public class ClienteLayoutEDI implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long idClienteLayoutEDI;
	
	private String nomeArquivo;
	
	private String email;
	
	private String ftpCaminho;
	
	private String ftpUser;
	
	private String ftpSenha;
	
	private String ftpServidor;
	
	private String nmPasta;
	
	private String infComplementares;
		
	private DomainValue tpProcesso;
		
	private ClienteEDI clienteEDI;
	
	private LayoutEDI  layoutEDI;
	
	private TipoTransmissaoEDI transmissaoEDI;
	
	private ClienteEDIFilialEmbarcadora embarcadora;
	
	private Boolean blMigrado;	
	
	public Long getIdClienteLayoutEDI() {
		return idClienteLayoutEDI;
	}

	public void setIdClienteLayoutEDI(Long idClienteLayoutEDI) {
		this.idClienteLayoutEDI = idClienteLayoutEDI;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public TipoTransmissaoEDI getTransmissaoEDI() {
		return transmissaoEDI;
	}

	public void setTransmissaoEDI(TipoTransmissaoEDI transmissaoEDI) {
		this.transmissaoEDI = transmissaoEDI;
	}

	public ClienteEDI getClienteEDI() {
		return clienteEDI;
	}

	public void setClienteEDI(ClienteEDI clienteEDI) {
		this.clienteEDI = clienteEDI;
	}

	public LayoutEDI getLayoutEDI() {
		return layoutEDI;
	}

	public void setLayoutEDI(LayoutEDI layoutEDI) {
		this.layoutEDI = layoutEDI;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFtpCaminho() {
		return ftpCaminho;
	}

	public void setFtpCaminho(String ftpCaminho) {
		this.ftpCaminho = ftpCaminho;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpSenha() {
		return ftpSenha;
	}

	public void setFtpSenha(String ftpSenha) {
		this.ftpSenha = ftpSenha;
	}

	public String getNmPasta() {
		return nmPasta;
	}

	public void setNmPasta(String nmPasta) {
		this.nmPasta = nmPasta;
	}

	public String getInfComplementares() {
		return infComplementares;
	}

	public void setInfComplementares(String infComplementares) {
		this.infComplementares = infComplementares;
	}

	public ClienteEDIFilialEmbarcadora getEmbarcadora() {
		return embarcadora;
	}

	public void setEmbarcadora(ClienteEDIFilialEmbarcadora embarcadora) {
		this.embarcadora = embarcadora;
	}

	public String getFtpServidor() {
		return ftpServidor;
	}

	public void setFtpServidor(String ftpServidor) {
		this.ftpServidor = ftpServidor;
	}
	
	public DomainValue getTpProcesso() {
		return tpProcesso;
}

	public void setTpProcesso(DomainValue tpProcesso) {
		this.tpProcesso = tpProcesso;
	}
	public Boolean getBlMigrado() {
		return blMigrado;
	}

	public void setBlMigrado(Boolean blMigrado) {
		this.blMigrado = blMigrado;
	}

	
}
