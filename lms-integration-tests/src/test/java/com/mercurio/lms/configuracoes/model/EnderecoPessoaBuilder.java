package com.mercurio.lms.configuracoes.model;

import com.mercurio.lms.municipios.model.Municipio;

public class EnderecoPessoaBuilder {

	private EnderecoPessoa endereco;
	
	private EnderecoPessoaBuilder() {
		endereco = new EnderecoPessoa();
	}
	
	public static EnderecoPessoaBuilder newEndereco() {
		return new EnderecoPessoaBuilder();
	}
	
	public EnderecoPessoaBuilder municipio(Municipio municipio) {
		endereco.setMunicipio(municipio);
		return this;
	}

	public EnderecoPessoa build() {
		return endereco;
	}
}
