package com.mercurio.lms.layoutNfse.model.cancelamento;

public class InfPedidoCancelamento {
	
	private String Id;
	private Long Numero;
	private String Cnpj;
	private String InscricaoMunicipal;
	private String CodigoMunicipio;
	private String CodigoCancelamento;
	private String Versao;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public Long getNumero() {
		return Numero;
	}
	public void setNumero(Long numero) {
		Numero = numero;
	}
	public String getCnpj() {
		return Cnpj;
	}
	public void setCnpj(String cnpj) {
		Cnpj = cnpj;
	}
	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		InscricaoMunicipal = inscricaoMunicipal;
	}
	public String getCodigoMunicipio() {
		return CodigoMunicipio;
	}
	public void setCodigoMunicipio(String codigoMunicipio) {
		CodigoMunicipio = codigoMunicipio;
	}
	public String getCodigoCancelamento() {
		return CodigoCancelamento;
	}
	public void setCodigoCancelamento(String codigoCancelamento) {
		CodigoCancelamento = codigoCancelamento;
	}
	public String getVersao() {
		return Versao;
	}
	public void setVersao(String versao) {
		Versao = versao;
	}
	
}
