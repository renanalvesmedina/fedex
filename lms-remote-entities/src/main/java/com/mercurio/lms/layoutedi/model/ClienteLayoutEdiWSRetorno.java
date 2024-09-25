package com.mercurio.lms.layoutedi.model;

import java.util.Map;

/**
 * Classe responsável por carregar as informações dos clientes do EDI necessárias para o WS.
 * @author ThiagoFA
 */
public class ClienteLayoutEdiWSRetorno {

	private Long idClienteLayoutEDI; 
	private String nomeArquivo;
	private String ftpCaminho;
	private String ftpUser;
	private String ftpSenha;
	private String ftpServidor;
	private String nmPasta;
	private Long idTransmissaoEDI;
	private Long idLayoutEDI;
	private Boolean blMigrado;
	private Map<String, String> quebraArquivo;
	
	public String getFtpServidor() {
		return ftpServidor;
	}
	public void setFtpServidor(String ftpServidor) {
		this.ftpServidor = ftpServidor;
	}
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
	public Long getIdTransmissaoEDI() {
		return idTransmissaoEDI;
	}
	public void setIdTransmissaoEDI(Long idTransmissaoEDI) {
		this.idTransmissaoEDI = idTransmissaoEDI;
	}
	public Long getIdLayoutEDI() {
		return idLayoutEDI;
	}
	public void setIdLayoutEDI(Long idLayoutEDI) {
		this.idLayoutEDI = idLayoutEDI;
	}
	public Map<String, String> getQuebraArquivo() {
		return quebraArquivo;
	}
	public void setQuebraArquivo(Map<String, String> quebraArquivo) {
		this.quebraArquivo = quebraArquivo;
	}
	public Boolean getBlMigrado() {
		return blMigrado;
	}
	public void setBlMigrado(Boolean blMigrado) {
		this.blMigrado = blMigrado;
	}
}
