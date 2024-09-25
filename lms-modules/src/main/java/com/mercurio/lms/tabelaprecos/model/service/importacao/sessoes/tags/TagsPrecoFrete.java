package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags;

import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_PESO_MINIMO_PRECO_FRETE;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_PRECO_FRETE;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_PRECO_FRETE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;

public class TagsPrecoFrete extends GerenciadorTags {

	private static final int GRUPO_PREFIXO = 2;

	public static final List<String> TAGS = Arrays.asList(new String[]{ FORMATO_TAG_VALOR_PRECO_FRETE, FORMATO_TAG_PESO_MINIMO_PRECO_FRETE });

	public static boolean ehTagPrecoFrete(String tag) {
		if (StringUtils.isBlank(tag)) {
			return false;
		}
		return Pattern.matches(FORMATO_TAG_PRECO_FRETE, tag);
	}

	public boolean colunaEhPesoFrete(int coluna){
		return this.tags.containsKey(coluna);
	}

	@Override
	public boolean atendeRequisitosMinimos() {
		Set<String> prefixos = this.prefixos();
		for (String prefixo : prefixos) {
			if (this.contem(FORMATO_TAG_PESO_MINIMO_PRECO_FRETE, prefixo) && !this.contem(FORMATO_TAG_VALOR_PRECO_FRETE, prefixo)) {
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
		return Arrays.asList(FORMATO_TAG_VALOR_PRECO_FRETE);
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
		Pattern pattern = Pattern.compile(FORMATO_TAG_PRECO_FRETE);
		Matcher matcher = pattern.matcher(tag.tag());
		if (!matcher.matches()) {
			return "";
		}
		return matcher.group(GRUPO_PREFIXO);
	}


}
