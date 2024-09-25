package com.mercurio.lms.layoutNfse.model.rps;


public class Tomador {
	private String RazaoSocial;
	private IdentificacaoTomador IdentificacaoTomador;
	private Endereco EnderecoTomador;
	private Contato Contato;
	private DadosComplementaresTomador DadosComplementaresTomador;

	public IdentificacaoTomador getIdentificacaoTomador() {
		return IdentificacaoTomador;
	}
	public void setIdentificacaoTomador(IdentificacaoTomador identificacaoTomador) {
		IdentificacaoTomador = identificacaoTomador;
	}
	public String getRazaoSocial() {
		return RazaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		RazaoSocial = razaoSocial;
	}
	public Endereco getEnderecoTomador() {
		return EnderecoTomador;
	}
	public void setEnderecoTomador(Endereco enderecoTomador) {
		EnderecoTomador = enderecoTomador;
	}
	public Contato getContato() {
		return Contato;
	}
	public void setContato(Contato contato) {
		Contato = contato;
	}
	public DadosComplementaresTomador getDadosComplementaresTomador() {
		return DadosComplementaresTomador;
	}
	public void setDadosComplementaresTomador(
			DadosComplementaresTomador dadosComplementaresTomador) {
		DadosComplementaresTomador = dadosComplementaresTomador;
	}

}
