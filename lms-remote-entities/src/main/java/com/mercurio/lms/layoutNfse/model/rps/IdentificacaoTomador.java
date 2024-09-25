package com.mercurio.lms.layoutNfse.model.rps;

public class IdentificacaoTomador {
	
	private String Cnpj;
	private String Cpf;
	private String InscricaoMunicipal;

	public String getCpf() {
		return Cpf;
	}

	public void setCpf(String cpf) {
		this.Cpf = cpf;
	}

	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.InscricaoMunicipal = inscricaoMunicipal;
	}

	public String getCnpj() {
		return Cnpj;
	}

	public void setCnpj(String cnpj) {
		this.Cnpj = cnpj;
	}

}
