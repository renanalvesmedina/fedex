package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags;

import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_FAIXA_PROGRESSIVA;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_FATOR_MULTIPLICACAO_FAIXA_PROGRESSIVA;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_MINIMO_PROGRESSIVO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_PESO_MINIMO_FAIXA_PROGRESSIVA;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_PRODUTO_ESPECIFICO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_TIPO_IND_CALC;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_UNIDADE_MEDIDA;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;

public class TagsFaixaProgressiva  extends GerenciadorTags {

	private static final int GRUPO_PREFIXO = 2;

	public static final List<String> TAGS = Arrays.asList(new String[] { FORMATO_TAG_FAIXA_PROGRESSIVA,
							FORMATO_TAG_TIPO_IND_CALC, FORMATO_TAG_MINIMO_PROGRESSIVO, FORMATO_TAG_UNIDADE_MEDIDA,
							FORMATO_TAG_PRODUTO_ESPECIFICO, FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA, FORMATO_TAG_PESO_MINIMO_FAIXA_PROGRESSIVA, FORMATO_TAG_FATOR_MULTIPLICACAO_FAIXA_PROGRESSIVA});
	private static final List<String> TAGS_VALOR_FAIXA = Arrays.asList(new String[] {FORMATO_TAG_PRODUTO_ESPECIFICO, FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA, FORMATO_TAG_PESO_MINIMO_FAIXA_PROGRESSIVA, FORMATO_TAG_FATOR_MULTIPLICACAO_FAIXA_PROGRESSIVA});

	Map<Integer, String> indiceTags = new HashMap<Integer, String>();

	@Override
	public void informa(String tag, int linha, int coluna) {
		super.informa(tag, linha, coluna);
		this.indiceTags.put(coluna, tag);
	}

	public boolean colunaEhProgressao(int indiceColuna) {
		return this.indiceTags.containsKey(indiceColuna);
	}

	public static boolean ehTagFaixaProgressiva(String tag) {
		return Pattern.matches(FORMATO_TAG_FAIXA_PROGRESSIVA, tag);
	}

	public static boolean tagEhCodigoValorFaixa(String tag) {
		if (StringUtils.isBlank(tag)) {
			return false;
		}
		for (String formato : TAGS_VALOR_FAIXA) {
			if (Pattern.matches(formato, tag.trim())) {
				return true;
			}
		}
		return false;
	}

	public static boolean tagEhPesoMinimo(String tag) {
		return Pattern.matches(FORMATO_TAG_PESO_MINIMO_FAIXA_PROGRESSIVA, tag);
	}

	public static boolean tagEhFatorMultiplicacao(String tag) {
		return Pattern.matches(FORMATO_TAG_FATOR_MULTIPLICACAO_FAIXA_PROGRESSIVA, tag);
	}
	
	public static boolean tagEhCodigoProdutoEspecifico(String tag) {
		return Pattern.matches(FORMATO_TAG_PRODUTO_ESPECIFICO, tag);
	}
	
	public static boolean tagEhValorFaixa(String tag) {
		return Pattern.matches(FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA, tag);
	}

	public static boolean tagEhTipoIndicadorCalculo(String tag) {
		return Pattern.matches(FORMATO_TAG_TIPO_IND_CALC, tag);
	}
	
	public static boolean tagEhMinimoProgressivo(String tag) {
		return Pattern.matches(FORMATO_TAG_MINIMO_PROGRESSIVO, tag);
	}
	
	public static boolean tagEhUnidadeMedida(String tag) {
		return Pattern.matches(FORMATO_TAG_UNIDADE_MEDIDA, tag);
	}
	
	@Override
	public boolean atendeRequisitosMinimos() {
		Set<String> prefixos = this.prefixos();
		for (String prefixo : prefixos) {
			if (!((this.contem(FORMATO_TAG_PRODUTO_ESPECIFICO, prefixo) || this.contem(FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA, prefixo))
					&& this.contem(FORMATO_TAG_MINIMO_PROGRESSIVO, prefixo) && this.contem(FORMATO_TAG_TIPO_IND_CALC, prefixo))) {
				return false;
			}
		}
		return true;
	}

	private boolean contem(String formato, String prefixo) {
		for (TagImportacao tag : tags.values()) {
			String prefixoTag = this.extraiPrefixo(tag);
			if (Pattern.matches(formato, tag.tag()) && prefixoTag.equals(prefixo)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> obrigatorias() {
		return Arrays.asList(FORMATO_TAG_MINIMO_PROGRESSIVO, FORMATO_TAG_TIPO_IND_CALC, String.format("%s ou %s", FORMATO_TAG_PRODUTO_ESPECIFICO, FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA));
	}

	public Set<String> prefixos() {
		Set<String> valores = new HashSet<String>();
		for (TagImportacao tag : this.tags.values()) {
			String prefixo = extraiPrefixo(tag);
			valores.add(prefixo);
		}
		return valores;
	}

	private String extraiPrefixo(TagImportacao tag) {
		Pattern pattern = Pattern.compile(FORMATO_TAG_FAIXA_PROGRESSIVA);
		Matcher matcher = pattern.matcher(tag.tag());
		if (!matcher.matches()) {
			return "";
		}
		return matcher.group(GRUPO_PREFIXO);
	}


}
