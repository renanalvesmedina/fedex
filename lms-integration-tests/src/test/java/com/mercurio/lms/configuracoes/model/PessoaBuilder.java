package com.mercurio.lms.configuracoes.model;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.MunicipioBuilder;

public class PessoaBuilder {

	private static final DomainValue TP_IDENTIFICACAO_CNPJ = new DomainValue("CNPJ");
	private static final DomainValue TP_IDENTIFICACAO_CPF = new DomainValue("CPF");

	private Pessoa pessoa;
	
	private PessoaBuilder() {
		pessoa = new Pessoa();
	}
	
	public static PessoaBuilder newPessoa() {
		return new PessoaBuilder();
	}
	
	public PessoaBuilder id(long id) {
		pessoa.setIdPessoa(id);
		return this;
	}

	public PessoaBuilder nome(String nome) {
		pessoa.setNmPessoa(nome);
		return this;
	}

	public PessoaBuilder cnpj(String cnpj) {
		pessoa.setTpIdentificacao(TP_IDENTIFICACAO_CNPJ);
		pessoa.setNrIdentificacao(cnpj);
		return this;
	}

	public PessoaBuilder cpf(String cpf) {
		pessoa.setTpIdentificacao(TP_IDENTIFICACAO_CPF);
		pessoa.setNrIdentificacao(cpf);
		return this;
	}

	public PessoaBuilder endereco(EnderecoPessoa endereco) {
		pessoa.setEnderecoPessoa(endereco);
		return this;
	}
	
	public Pessoa build() {
		return pessoa;
	}
	
	public Pessoa portoAlegre() {
		EnderecoPessoa endereco = EnderecoPessoaBuilder
						.newEndereco()
						.municipio(MunicipioBuilder.PORTO_ALEGRE())
						.build();
		pessoa.setEnderecoPessoa(endereco);
		return pessoa;
	}

}
