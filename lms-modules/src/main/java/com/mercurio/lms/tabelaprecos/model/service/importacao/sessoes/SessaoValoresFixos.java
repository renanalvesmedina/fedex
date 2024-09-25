package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.GenericoImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

public class SessaoValoresFixos implements SessaoArquivoImportacao {

	private static final int GRUPO_PREFIXO = 2;

	private Map<String, GenericoImportacao> genericos = new HashMap<String, GenericoImportacao>();
	private String tagEmAberto;
	private GenericoImportacao genericoEmAberto;

	@Override
	public Map<TipoComponente, List<ComponenteImportacao>> resultadoImportacao() {
		Map<TipoComponente, List<ComponenteImportacao>> resultado = new HashMap<ComponenteImportacao.TipoComponente, List<ComponenteImportacao>>();
		@SuppressWarnings("unchecked")
		List<ComponenteImportacao> valores = new ArrayList<ComponenteImportacao>(this.genericos.values());
		resultado.put(TipoComponente.GENERICO, valores);
		return resultado;
	}

	public void informaConteudo(int linha, int coluna, String valor) {
		if (TagsImportacaoTabelaPreco.ehTag(valor)) {
			informaTag(linha, coluna, valor);
			return;
		}

		if (this.genericoEmAberto != null) {
			this.genericoEmAberto.incluiValor(linha, coluna, valor);
			this.genericoEmAberto = null;
		}
	}

	private void informaTag(int linha, int coluna, String valor) {
		validaTag(linha, coluna);
		String prefixo = this.extraiPrefixo(valor);
		if (StringUtils.isBlank(prefixo)) {
			throw new ImportacaoException(coluna, linha, String.format("O valor '%s' não é uma tag de valores fixos válida.", valor));
		}
		GenericoImportacao generico = buscaGenerico(prefixo);
		generico.incluiTag(linha, coluna, valor);
		this.genericoEmAberto = generico;
		this.tagEmAberto = valor;
		return;
	}

	private void validaTag(int linha, int coluna) {
		if (this.genericoEmAberto != null) {
			throw new ImportacaoException(coluna, linha,
					String.format("Você não pode informar uma tag aqui, pois a tag '%s' ainda está em aberto. Informe um valor para '%s' antes de informar uma nova tag.", this.tagEmAberto, this.tagEmAberto));
		}
	}

	private GenericoImportacao buscaGenerico(String prefixo) {
		if (this.genericos.containsKey(prefixo)) {
			return this.genericos.get(prefixo);
		}
		GenericoImportacao generico = new GenericoImportacao();
		this.genericos.put(prefixo, generico);
		return generico;
	}

	private String extraiPrefixo(String valor) {
		Pattern patternFixo = Pattern.compile(TagsImportacaoTabelaPreco.FORMATO_TAG_GENERICO);
		Matcher matcherFixo = patternFixo.matcher(valor);
		if (matcherFixo.matches() && !TagsImportacaoTabelaPreco.ehTagTabela(valor)) {
			return matcherFixo.group(GRUPO_PREFIXO);
		}
		return null;
	}

}
