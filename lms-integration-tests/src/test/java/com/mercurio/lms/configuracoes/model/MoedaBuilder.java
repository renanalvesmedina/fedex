package com.mercurio.lms.configuracoes.model;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

public class MoedaBuilder {

	private Moeda moeda;

	private MoedaBuilder() {
		moeda = new Moeda();
		moeda.setTpSituacao(new DomainValue("A"));
	}

	public static MoedaBuilder newMoeda() {
		return new MoedaBuilder();
	}

	public MoedaBuilder id(long id) {
		moeda.setIdMoeda(id);
		return this;
	}

	public MoedaBuilder descricao(String descricao) {
		moeda.setDsMoeda(new VarcharI18n(descricao));
		return this;
	}

	public MoedaBuilder sigla(String sigla) {
		moeda.setSgMoeda(sigla);
		return this;
	}

	public MoedaBuilder simbolo(String simbolo) {
		moeda.setDsSimbolo(simbolo);
		return this;
	}

	public MoedaBuilder inativo() {
		moeda.setTpSituacao(new DomainValue("I"));
		return this;
	}

	public MoedaBuilder extenso(String extenso) {
		moeda.setDsValorExtenso(new VarcharI18n(extenso));
		return this;
	}

	public MoedaBuilder isoCode(int isoCode) {
		moeda.setNrIsoCode((short) isoCode);
		return this;
	}

	public Moeda build() {
		return moeda;
	}

	public static Moeda BRL() {
		return newMoeda()
				.id(1)
				.descricao("Real")
				.sigla("BRL")
				.simbolo("R$")
				.extenso("Centavo;Centavos;Real;Reais")
				.isoCode(986)
				.build();
	}

	public static Moeda USD() {
		return newMoeda()
				.id(2)
				.descricao("D�lar Americano")
				.sigla("USD")
				.simbolo("U$S")
				.extenso("Centavo;Centavos;D�lar;D�lares")
				.isoCode(840)
				.build();
	}

}
