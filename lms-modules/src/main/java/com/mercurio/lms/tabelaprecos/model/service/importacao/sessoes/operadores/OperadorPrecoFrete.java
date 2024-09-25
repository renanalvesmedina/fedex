package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.GrupoPrecoFreteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.OperadorTagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsPrecoFrete;

public class OperadorPrecoFrete implements OperadorTagImportacao, Observador {

	private static final int GRUPO_PREFIXO = 2;
	private static final String MENSAGEM_OBRIGATORIA_SINGULAR = "A tag %s é obrigatoria para informar uma tabela de preço frete.";
	private static final String MENSAGEM_OBRIGATORIA_PLURAL = "As tags %s são obrigatorias para informar uma tabela de preço frete.";

	private TagsPrecoFrete tags = new TagsPrecoFrete();
	private Map<String, GrupoPrecoFreteImportacao> gruposPorPrefixo = new HashMap<String, GrupoPrecoFreteImportacao>();
	private Map<Integer, GrupoPrecoFreteImportacao> gruposPorColuna = new HashMap<Integer, GrupoPrecoFreteImportacao>();

	private ChaveProgressao chave;

	@Override
	public boolean incluiTag(int linha, int coluna, String tag) {
		if(!TagsPrecoFrete.ehTagPrecoFrete(tag)){
			throw new ImportacaoException(linha, coluna, String.format("A tag '%s' não é uma tag de proço frete válida", tag));
		}
		tags.informa(tag, linha, coluna);
		String prefixo = this.extrairPrefixo(tag);
		if (!this.gruposPorPrefixo.containsKey(prefixo)) {
			this.gruposPorPrefixo.put(prefixo, new GrupoPrecoFreteImportacao(prefixo, this.tags.tag(coluna)));
		}
		GrupoPrecoFreteImportacao grupo = this.gruposPorPrefixo.get(prefixo);
		this.gruposPorColuna.put(coluna, grupo);
		return true;
	}

	private String extrairPrefixo(String valor) {
		Pattern padrao = Pattern.compile(TagsImportacaoTabelaPreco.FORMATO_TAG_PRECO_FRETE);
		Matcher matcher = padrao.matcher(valor);
		if (matcher.matches()) {
			return matcher.group(GRUPO_PREFIXO);
		}
		return "";
	}

	@Override
	public boolean incluiFaixa(int linha, int coluna, String tag) {
		return true;
	}

	@Override
	public boolean incluiValor(int linha, int coluna, String valor) {
		if(!tags.colunaEhPesoFrete(coluna)){
			throw new ADSMException(String.format("A coluna %d não é uma coluna de preço frete", coluna));
		}

		GrupoPrecoFreteImportacao grupo = this.gruposPorColuna.get(coluna);
		grupo.incluiValor(new ValorImportacao(linha, coluna, valor, this.tags.tag(coluna)));

		if(chave != null){
			grupo.informaChave(chave);
		}
		return true;
	}

	@Override
	public void atualiza(Observavel observavel) {
		try {
			this.chave = ((OperadorObservavel) observavel).retornaChave();
		} catch (ImportacaoException e) {
			this.reiniciaGrupos();
			throw e;
		}
		this.atualizaPrecoFretePendente();
		return;
	}
	
	private void reiniciaGrupos() {
		for (GrupoPrecoFreteImportacao grupo : this.gruposPorPrefixo.values()) {
			grupo.cancela();
		}
	}

	private void atualizaPrecoFretePendente() {
		for (GrupoPrecoFreteImportacao grupo : this.gruposPorPrefixo.values()) {
			grupo.informaChave(this.chave);
		}
	}

	@Override
	public List<ComponenteImportacao> resultadoImportacao() {
		Set<ComponenteImportacao> resultadosExclusivos = new HashSet<ComponenteImportacao>(this.gruposPorPrefixo.values());
		return new ArrayList<ComponenteImportacao>(resultadosExclusivos);
	}

	@Override
	public TipoComponente tipoOperador() {
		return TipoComponente.PRECO_FRETE;
	}

	@Override
	public void validaTags() {
		if (!this.tags.atendeRequisitosMinimos()) {
			TagImportacao primeira = this.tags.primeira();
			List<String> tagsObrigatorias = this.tags.obrigatorias();
			String mensagem = tagsObrigatorias.size() > 1? MENSAGEM_OBRIGATORIA_PLURAL : MENSAGEM_OBRIGATORIA_SINGULAR;
			throw new ImportacaoExceptionBloqueante(primeira.coluna(), primeira.linha(), String.format(mensagem, StringUtils.join(tagsObrigatorias.iterator(), ", ")));
		}

	}

	@Override
	public void reinicia() {
		this.chave = null;
		for (GrupoPrecoFreteImportacao grupo : this.gruposPorPrefixo.values()) {
			grupo.novoRegistro();
		}
	}

	@Override
	public void encerraLinha() {
		//intencionalmente deixado em branco pois não há necessidade de faze-lo	
	}



}
