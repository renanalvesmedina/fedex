package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

public class AgentesOperadorChaveProgressao {

	private Map<Integer, AgenteChaveProgressao> agentes = new HashMap<Integer, AgenteChaveProgressao>();
	private Map<TipoAgente, AgenteChaveProgressao> agentesPorTipo = new HashMap<TipoAgente, AgenteChaveProgressao>();


	public AgenteChaveProgressao incluiAgenteParaTag(int linha, int coluna, String tag) {
		if (this.agentes.containsKey(coluna)) {
			return this.agentes.get(coluna);
		}
		TipoAgente tipo = TipoAgente.tipoAgentePara(linha, coluna, tag);
		TagImportacao tagImportacao = new TagImportacao(linha, coluna, tag);
		if (this.agentesPorTipo.containsKey(tipo)) {
			AgenteChaveProgressao agente = this.agentesPorTipo.get(tipo);
			informaTag(coluna, tagImportacao, agente);
			return agente;
		}
		AgenteChaveProgressao agente = criaAgente(tipo);
		informaTag(coluna, tagImportacao, agente);
		this.agentesPorTipo.put(tipo, agente);
		return agente;
	}

	private AgenteChaveProgressao criaAgente(TipoAgente tipo) {
		CriadorAgente criador = tipo.criadorDeAgente();
		criador.informaAgentes(new HashSet<AgenteChaveProgressao>(this.agentes.values()));
		return criador.cria();
	}

	private void informaTag(int coluna, TagImportacao tagImportacao,
			AgenteChaveProgressao agente) {
		agente.informaTag(tagImportacao);
		this.agentes.put(coluna, agente);
	}

	public AgenteChaveProgressao agenteParaColuna(int coluna) {
		if (this.agentes.containsKey(coluna)) {
			return this.agentes.get(coluna);
		}
		return null;
	}

	public void reinicia() {
		for (AgenteChaveProgressao agente : this.agentesPorTipo.values()) {
			agente.reseta();
		}
	}

	public boolean estahCompleto() {
		if (this.agentesPorTipo.containsKey(TipoAgente.TARIFA_X_ROTA)) {
			return this.agentesPorTipo.get(TipoAgente.TARIFA_X_ROTA).estahCompleto();
		}
		return this.agentes.values().iterator().next().estahCompleto();
	}

	public ChaveProgressao retornaChave() {
		if (this.agentesPorTipo.containsKey(TipoAgente.TARIFA_X_ROTA)) {
			return this.agentesPorTipo.get(TipoAgente.TARIFA_X_ROTA).chave();
		}
		return this.agentes.values().iterator().next().chave();
	}

	public void validaTags() {
		for (AgenteChaveProgressao agente : this.agentesPorTipo.values()) {
			agente.validaTags();
		}
		if (this.agentesPorTipo.containsKey(TipoAgente.TARIFA_X_ROTA) &&
				!(this.agentesPorTipo.containsKey(TipoAgente.TARIFA) && this.agentesPorTipo.containsKey(TipoAgente.ROTA))) {
			disparaExcecaoTagsIncompletas();
		}
		if (!this.agentesPorTipo.containsKey(TipoAgente.TARIFA_X_ROTA) &&
			 (this.agentesPorTipo.containsKey(TipoAgente.TARIFA) && this.agentesPorTipo.containsKey(TipoAgente.ROTA))) {
			disparaExcecaoTagsIncompativeis();
		}
	}

	private void disparaExcecaoTagsIncompletas() {
		AgenteChaveProgressao agente = this.agentesPorTipo.get(TipoAgente.TARIFA_X_ROTA);
		boolean temTarifa = this.agentesPorTipo.containsKey(TipoAgente.TARIFA);
		String mensagem;
		if (temTarifa) {
			mensagem = "rotas";
		} else {
			mensagem = "tarifas";
		}
		throw new ImportacaoExceptionBloqueante(agente.coluna(), agente.linha(), String.format("Para importar tarifas e rotas, você deve informar %s válidas.", mensagem));
	}
	
	private void disparaExcecaoTagsIncompativeis() {
		AgenteChaveProgressao agenteTarifa = this.agentesPorTipo.get(TipoAgente.TARIFA);
		AgenteChaveProgressao agenteRota = this.agentesPorTipo.get(TipoAgente.ROTA);
		int coluna = Math.min(agenteTarifa.coluna(), agenteRota.coluna());
		int linha  = Math.min(agenteTarifa.coluna(), agenteRota.coluna());
		throw new ImportacaoExceptionBloqueante(coluna, linha, String.format("Para importar tarifas e rotas, você deve informar a tag %s.", TagsImportacaoTabelaPreco.TAG_TARIFA_X_ROTA));
	}

	public void encerraLinha() {
		for (AgenteChaveProgressao agente : this.agentesPorTipo.values()) {
			agente.encerraLinha();
		}
		
	}

}
