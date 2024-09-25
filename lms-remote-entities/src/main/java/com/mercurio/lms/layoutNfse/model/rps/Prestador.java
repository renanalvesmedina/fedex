package com.mercurio.lms.layoutNfse.model.rps;


public class Prestador {
	private String Cnpj;
	private String InscricaoMunicipal;
	private Endereco EnderecoPrestador;
	private DadosComplementaresPrestador DadosComplementaresPrestador;

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
	public DadosComplementaresPrestador getDadosComplementaresPrestador() {
		return DadosComplementaresPrestador;
	}
	public void setDadosComplementaresPrestador(
			DadosComplementaresPrestador dadosComplementaresPrestador) {
		DadosComplementaresPrestador = dadosComplementaresPrestador;
	}
	public Endereco getEnderecoPrestador() {
		return EnderecoPrestador;
	}
	public void setEnderecoPrestador(Endereco enderecoPrestador) {
		EnderecoPrestador = enderecoPrestador;
	}
	
}
