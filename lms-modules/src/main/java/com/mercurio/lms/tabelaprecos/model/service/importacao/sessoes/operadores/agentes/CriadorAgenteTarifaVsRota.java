package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import java.util.Set;

public class CriadorAgenteTarifaVsRota implements CriadorAgente {

	private Set<AgenteChaveProgressao> agentes;

	@Override
	public AgenteChaveProgressao cria() {
		AgenteTarifaVsRota agente = new AgenteTarifaVsRota();
		for (AgenteChaveProgressao agenteChaveProgressao : agentes) {
			if (AgenteObservavel.class.isInstance(agenteChaveProgressao)) {
				((AgenteObservavel) agenteChaveProgressao).incluiObservador(agente);
			}
		}
		return agente;
	}

	@Override
	public void informaAgentes(Set<AgenteChaveProgressao> agentes) {
		this.agentes = agentes;
	}

}
