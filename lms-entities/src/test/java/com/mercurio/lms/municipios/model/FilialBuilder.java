package com.mercurio.lms.municipios.model;

import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.PessoaBuilder;
import com.mercurio.lms.municipios.model.Filial;

public class FilialBuilder {

	private Filial filial;

	private FilialBuilder() {
		filial = new Filial();
	}

	public static FilialBuilder newFilial() {
		return new FilialBuilder();
	}

	public FilialBuilder id(long id) {
		filial.setIdFilial(id);
		return this;
	}

	public FilialBuilder sigla(String sigla) {
		filial.setSgFilial(sigla);
		return this;
	}

	public FilialBuilder pessoa(Pessoa pessoa) {
		filial.setPessoa(pessoa);
		return this;
	}

	public Filial build() {
		return filial;
	}

	public Filial portoAlegre() {
		filial.setPessoa(PessoaBuilder.newPessoa().portoAlegre());
		return filial;
	}

	public static Filial POA() {
		return buildFilial(370L, "POA", "Porto Alegre");
	}

	public static Filial SAO() {
		return buildFilial(378L, "SAO", "São Paulo - Remédios");
	}

	public static Filial CWB() {
		return buildFilial(329L, "CWB", "Curitiba");
	}

	public static Filial UBL() {
		return buildFilial(398L, "UBL", "Uberlândia");
	}

	private static Filial buildFilial(long id, String sigla, String nome) {
		return newFilial()
				.id(id)
				.sigla(sigla)
				.pessoa(PessoaBuilder.newPessoa()
						.nome(nome)
						.build())
				.build();
	}

}
