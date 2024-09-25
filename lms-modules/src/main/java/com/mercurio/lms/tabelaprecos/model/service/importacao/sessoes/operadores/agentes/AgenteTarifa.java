package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.CodigoTarifaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

public class AgenteTarifa extends AgenteObservavel {

	private TagImportacao tag;
	private CodigoTarifaImportacao tarifa;

	@Override
	public void informaTag(TagImportacao tagImportacao) {
		this.tag = tagImportacao;
	}

	@Override
	public boolean respondePor(String tag) {
		if (StringUtils.isBlank(tag)) {
			return false;
		}
		return TagsImportacaoTabelaPreco.TAG_COD_TARIFA.equals(tag);
	}

	@Override
	public TipoAgente tipo() {
		return TipoAgente.TARIFA;
	}

	@Override
	public ChaveProgressao chave() {
		return this.tarifa;
	}

	@Override
	public void executaIncluiValor(int linha, int coluna, String valor) {
		if (StringUtils.isBlank(valor)) {
			throw new ImportacaoException(coluna, linha, "Você deve informar um código de tarifa válido.");
		}
		this.tarifa = new CodigoTarifaImportacao(new ValorImportacao(linha, coluna, valor, this.tag));
	}

	@Override
	public void reseta() {
		this.tarifa = null;
	}

	@Override
	public boolean estahCompleto() {
		return this.tarifa != null && this.tarifa.estahCompleta();
	}

	@Override
	public void validaTags() {
		if (tag == null || StringUtils.isBlank(tag.tag())) {
			throw new ImportacaoExceptionBloqueante(0, 0, "Uma tag de código de tarifa era esperada.");
		}
		
	}

	@Override
	public int linha() {
		return this.tag.linha();
	}

	@Override
	public int coluna() {
		return this.tag.coluna();
	}

}
