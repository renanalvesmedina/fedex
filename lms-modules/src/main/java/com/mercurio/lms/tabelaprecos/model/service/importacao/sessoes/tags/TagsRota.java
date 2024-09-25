package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags;

import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_AERO_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_AERO_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_FILIAL_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_FILIAL_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_GRUPO_REG_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_GRUPO_REG_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_MUNICIPIO_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_MUNICIPIO_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_PAIS_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_PAIS_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_COML_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_COML_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_UF_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_UF_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_ZONA_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_ZONA_ORIGEM;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.excessao.ErroImportacaoUtils;

public class TagsRota extends GerenciadorTags {


	public static final List<String> TAGS = Arrays.asList(new String[] {TAG_ZONA_ORIGEM, TAG_ZONA_DESTINO, TAG_TP_LOCAL_ORIGEM, TAG_TP_LOCAL_DESTINO,
				TAG_TP_LOCAL_COML_ORIGEM, TAG_TP_LOCAL_COML_DESTINO, TAG_AERO_ORIGEM, TAG_AERO_DESTINO, TAG_UF_ORIGEM, TAG_UF_DESTINO, TAG_PAIS_ORIGEM,
				TAG_PAIS_DESTINO, TAG_MUNICIPIO_ORIGEM, TAG_MUNICIPIO_DESTINO, TAG_FILIAL_ORIGEM, TAG_FILIAL_DESTINO, TAG_GRUPO_REG_ORIGEM, TAG_GRUPO_REG_DESTINO });

	Map<Integer, String> indiceTags = new HashMap<Integer, String>();

	@Override
	public void informa(String tag, int linha, int coluna) {
		TagImportacao tagJahInformada = this.buscaTag(tag);
		if (tagJahInformada != null) {
			throw new ImportacaoException(coluna, linha, String.format("A tag '%s' já foi informada na célula %s%d.", tag, this.converteColuna(tagJahInformada.coluna()), tagJahInformada.linha()));
		}
		super.informa(tag, linha, coluna);
		this.indiceTags.put(coluna, tag);
	}
	
	private String converteColuna(int coluna) {
		return new ErroImportacaoUtils().converteColuna(coluna);
	}

	private TagImportacao buscaTag(final String tag) {
		if (MapUtils.isEmpty(this.tags)) {
			return null;
		}
		return (TagImportacao) CollectionUtils.find(this.tags.values(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				TagImportacao tagImportacao = (TagImportacao) object;
				return tagImportacao.tag().equalsIgnoreCase(tag);
			}
		});
	}

	public boolean rotaCompleta() {
		for(String tag: TAGS) {
			if (!this.indiceTags.containsValue(tag)) {
				return false;
			}
		}
		return true;
	}

	public boolean colunaEhRota(int indiceColuna) {
		return this.indiceTags.containsKey(indiceColuna);
	}

	public static boolean ehTagRota(String tag) {
		return TAGS.contains(tag);
	}

	@Override
	public boolean atendeRequisitosMinimos() {
		boolean origemValida = this.indiceTags.containsValue(TAG_UF_ORIGEM);
		boolean destinoValido = this.indiceTags.containsValue(TAG_UF_DESTINO);
		return origemValida && destinoValido;
	}

	@Override
	public List<String> obrigatorias() {
		return Arrays.asList(" para origem: " + TAG_UF_ORIGEM + "; e para destino: " + TAG_UF_DESTINO);
	}

		

}
