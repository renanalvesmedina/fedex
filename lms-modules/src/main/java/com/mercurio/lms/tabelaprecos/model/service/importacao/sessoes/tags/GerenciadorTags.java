package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;

public abstract class GerenciadorTags {

	Map<Integer, TagImportacao> tags = null;

	public void informa(String tag, int linha, int coluna) {
		iniciaTags();
		TagImportacao tagImportacao = new TagImportacao(linha, coluna, tag);
		this.tags.put(coluna, tagImportacao);
	}
	
	
	private void iniciaTags() {
		if (this.tags == null) {
			this.tags = new HashMap<Integer, TagImportacao>();
		}
	}

	public TagImportacao tag(int indice) {
		return this.tags.get(indice);
	}

	public TagImportacao primeira() {
		Set<Integer> colunas = tags.keySet();
		List<Integer> listaColunas = new ArrayList<Integer>(colunas);
		Collections.sort(listaColunas);
		return tags.get(listaColunas.get(0));
	}

	public abstract boolean atendeRequisitosMinimos();

	public abstract List<String> obrigatorias();

}

