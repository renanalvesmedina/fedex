package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes.AgenteChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes.AgentesOperadorChaveProgressao;


public class OperadorChaveProgressao extends OperadorObservavel {

	private Map<Integer, TagImportacao> tagsPorColuna;
	private AgentesOperadorChaveProgressao agentes = new AgentesOperadorChaveProgressao();
	private ChaveProgressao chaveAtual;
	private Set<ChaveProgressao> chavesInformadas;
	
	@Override
	public boolean incluiTag(int linha, int coluna, String tag) {
		this.agentes.incluiAgenteParaTag(linha, coluna, tag);
		this.incluiTagPorColuna(coluna, new TagImportacao(linha, coluna, tag));
		return false;
	}

	private void incluiTagPorColuna(int coluna, TagImportacao tagImportacao) {
		if (this.tagsPorColuna == null) {
			this.tagsPorColuna = new HashMap<Integer, TagImportacao>();
		}
		this.tagsPorColuna.put(coluna, tagImportacao);
	}

	@Override
	public List<ComponenteImportacao> resultadoImportacao() {
		return Collections.emptyList();
	}

	@Override
	public TipoComponente tipoOperador() {
		return null;
	}

	@Override
	public void validaTags() {
		this.agentes.validaTags();
	}
	
	@Override
	public void reinicia() {
		this.agentes.reinicia();
	}

	@Override
	protected boolean executaIncluiValor(int linha, int coluna, String valor) {
		AgenteChaveProgressao agente = this.agentes.agenteParaColuna(coluna);
		agente.incluiValor(linha, coluna, valor);
		return true;
	}

	@Override
	protected boolean estahCompleto() {
		return this.agentes.estahCompleto();
	}

	@Override
	public ChaveProgressao retornaChave() {
		ChaveProgressao chave = this.agentes.retornaChave();
		if (chave.equals(chaveAtual) && (chave.linha() == chaveAtual.linha())) {
			return chave;
		}
		chaveAtual = chave;
		this.iniciaCacheChaves();
		if (this.chavesInformadas.contains(chaveAtual)) {
			ChaveProgressao chaveInformada = this.buscaChaveInformada(chaveAtual);
			int coluna = chaveAtual.coluna();
			int linha = chaveAtual.linha();
			String nomeTipoChave = chaveAtual.tipo().name().toLowerCase();
			chaveAtual = null;
			this.reinicia();
			throw new ImportacaoException(coluna, linha, String.format("A %s informada já foi informada na linha %d.", nomeTipoChave, chaveInformada.linha() + 1));
		}
		this.chavesInformadas.add(chaveAtual);
		return chaveAtual;
	}

	private void iniciaCacheChaves() {
		if (this.chavesInformadas == null) {
			this.chavesInformadas = new HashSet<ChaveProgressao>();
		}
	}

	private ChaveProgressao buscaChaveInformada(ChaveProgressao chaveAtual) {
		for (ChaveProgressao chave : this.chavesInformadas) {
			if (chave.equals(chaveAtual)) {
				return chave;
			}
		}
		return null;
	}

	@Override
	protected void executaEncerraLinha() {
		this.agentes.encerraLinha();		
	}
	

}
