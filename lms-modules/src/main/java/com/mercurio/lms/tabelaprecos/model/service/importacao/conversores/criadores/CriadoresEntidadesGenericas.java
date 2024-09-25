package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.criadores;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.CriadorEntidadeGenerica;


public class CriadoresEntidadesGenericas {

	Map<String, CriadorEntidadeGenerica> criadores = new HashMap<String, CriadorEntidadeGenerica>();
	Map<String, Class<? extends CriadorEntidadeGenerica>> classesCriadores = new HashMap<String, Class<? extends CriadorEntidadeGenerica>>();

	public CriadoresEntidadesGenericas() {
		classesCriadores.put("G", CriadorGeneralidade.class);
		classesCriadores.put("S", CriadorServicoAdicional.class);
		classesCriadores.put("T", CriadorTaxa.class);
	}

	public CriadorEntidadeGenerica criadorPara(String tipoParcela) {
		if (criadores.containsKey(tipoParcela)) {
			return criadores.get(tipoParcela);
		}
		if (!classesCriadores.containsKey(tipoParcela)) {
			throw new IllegalArgumentException(String.format("Tipo de parcela '%s' não possui classe de configurador correspondente.", tipoParcela));
		}
		Class<?> classe = this.classesCriadores.get(tipoParcela);
		CriadorEntidadeGenerica criador = cria(classe);
		this.criadores.put(tipoParcela, criador);
		return criador;
	}

	private CriadorEntidadeGenerica cria(Class<?> classe) {
		try {
			return (CriadorEntidadeGenerica) classe.newInstance();
		} catch (InstantiationException e) {
			throw new ADSMException(e);
		} catch (IllegalAccessException e) {
			throw new ADSMException(e);
		}
	}

}
