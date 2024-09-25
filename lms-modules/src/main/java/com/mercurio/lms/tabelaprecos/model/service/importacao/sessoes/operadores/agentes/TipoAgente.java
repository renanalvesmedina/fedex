package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsRota;

enum TipoAgente {

	TARIFA(new CriadorAgenteTarifa()), ROTA(new CriadorAgenteRota()), TARIFA_X_ROTA(
			new CriadorAgenteTarifaVsRota());

	private CriadorAgente criador;

	TipoAgente(CriadorAgente criador) {
		this.criador = criador;
	}

	public CriadorAgente criadorDeAgente() {
		return this.criador;
	}
	
	public static TipoAgente tipoAgentePara(int linha, int coluna, String tag) {
		if (TagsRota.ehTagRota(tag)) {
			return ROTA;
		}
		if (TagsImportacaoTabelaPreco.TAG_COD_TARIFA.equals(tag)) {
			return TARIFA;
		}
		if (TagsImportacaoTabelaPreco.TAG_TARIFA_X_ROTA.equals(tag)) {
			return TARIFA_X_ROTA;
		}
		throw new ImportacaoException(coluna, linha, String.format("A tag '%s' não é uma tag de chave de progressão válida.", tag));
	}
	
}
