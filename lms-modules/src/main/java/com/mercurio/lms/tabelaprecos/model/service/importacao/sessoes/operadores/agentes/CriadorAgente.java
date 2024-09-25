package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import java.util.Set;

public interface CriadorAgente {

	AgenteChaveProgressao cria();

	void informaAgentes(Set<AgenteChaveProgressao> hashSet);

}
