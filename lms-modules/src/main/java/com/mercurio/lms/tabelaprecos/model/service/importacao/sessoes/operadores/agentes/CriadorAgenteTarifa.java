package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import java.util.Set;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.Observador;

public class CriadorAgenteTarifa implements CriadorAgente {

	private Set<AgenteChaveProgressao> agentes;

	@Override
	public AgenteChaveProgressao cria() {
		AgenteTarifa agente = new AgenteTarifa();
		for (AgenteChaveProgressao agenteChaveProgressao : this.agentes) {
			if (Observador.class.isInstance(agenteChaveProgressao)) {
				agente.incluiObservador((Observador) agenteChaveProgressao);
			}
		}
		return agente;
	}

	@Override
	public void informaAgentes(Set<AgenteChaveProgressao> agentes) {
		this.agentes = agentes;
	}

}
