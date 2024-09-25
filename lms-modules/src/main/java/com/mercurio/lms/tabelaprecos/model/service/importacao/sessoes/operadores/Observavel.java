package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public abstract class Observavel {

	protected List<Observador> observadores = null;

	public void incluiObservador(Observador observador) {
		if (observadores == null) {
			this.observadores = new ArrayList<Observador>();
		}
		this.observadores.add(observador);
	}

	protected void atualiza() {
		if (CollectionUtils.isEmpty(this.observadores)) {
			return;
		}
		for (Observador observador : observadores) {
			observador.atualiza(this);
		}
	}

}
