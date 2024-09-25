package com.mercurio.lms.vendas.model;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.PessoaBuilder;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.util.ConstantesVendas;

public class ClienteBuilder {

	private static final DomainValue TP_CLIENTE_ESPECIAL = new DomainValue(ConstantesVendas.CLIENTE_ESPECIAL);
	private static final DomainValue TP_CLIENTE_FILIAL = new DomainValue(ConstantesVendas.CLIENTE_FILIAL);
	private static final DomainValue TP_CLIENTE_EVENTUAL = new DomainValue(ConstantesVendas.CLIENTE_EVENTUAL);
	private static final DomainValue TP_CLIENTE_POTENCIAL = new DomainValue(ConstantesVendas.CLIENTE_POTENCIAL);
	private Cliente cliente;

	private ClienteBuilder() {
		cliente = new Cliente();
	}

	public static ClienteBuilder newCliente() {
		return new ClienteBuilder();
	}

	public ClienteBuilder id(long id) {
		cliente.setIdCliente(id);
		return this;
	}

	public ClienteBuilder especial() {
		cliente.setTpCliente(TP_CLIENTE_ESPECIAL);
		return this;
	}

	public ClienteBuilder filial() {
		cliente.setTpCliente(TP_CLIENTE_FILIAL);
		return this;
	}

	public ClienteBuilder eventual() {
		cliente.setTpCliente(TP_CLIENTE_EVENTUAL);
		return this;
	}

	public ClienteBuilder potencial() {
		cliente.setTpCliente(TP_CLIENTE_POTENCIAL);
		return this;
	}

	public ClienteBuilder pessoa(Pessoa pessoa) {
		cliente.setPessoa(pessoa);
		return this;
	}

	public Cliente build() {
		return cliente;
	}

	public static Cliente APPLE_COMPUTER_00623904000173() {
		return buildCliente(1937221L, "APPLE COMPUTER BRASIL LTDA", "00623904000173");
	}

	public static Cliente APPLE_COMPUTER_00623904000335() {
		return buildCliente(3806611L, "APPLE COMPUTER BRASIL LTDA", "00623904000335");
	}

	public static Cliente APPLE_COMPUTER_99904999357239() {
		return buildCliente(4327147L, "APPLE COMPUTER BRASIL LTDA", "99904999357239");
	}

	public static Cliente NATURA_COSMETICOS_71673990003788() {
		return buildCliente(3882018L, "NATURA COSMETICOS S/A", "71673990003788");
	}

	private static Cliente buildCliente(long id, String nome, String cnpj) {
		return newCliente()
				.id(id)
				.pessoa(PessoaBuilder.newPessoa()
						.id(id)
						.nome(nome)
						.cnpj(cnpj)
						.build())
				.build();
	}

}
