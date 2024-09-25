package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.RotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsRota;

public class AgenteRota extends AgenteObservavel {

	private TagsRota tags = new TagsRota();
	private RotaImportacao rota;

	@Override
	public boolean respondePor(String tag) {
		if (StringUtils.isBlank(tag)) {
			return false;
		}
		return TagsRota.ehTagRota(tag);
	}

	@Override
	public void informaTag(TagImportacao tag) {
		if (!TagsRota.ehTagRota(tag.tag())) {
			return;
		}
		this.tags.informa(tag.tag(), tag.linha(), tag.coluna());
	}

	@Override
	public TipoAgente tipo() {
		return TipoAgente.ROTA;
	}

	@Override
	public ChaveProgressao chave() {
		if (this.rota == null) {
			return null;
		}
		this.rota.valida();
		return this.rota;
	}

	@Override
	public void executaIncluiValor(int linha, int coluna, String valor) {
		if (StringUtils.isBlank(valor)) {
			return;
		}
		if (this.rota == null) {
			this.rota = new RotaImportacao();
		}
		TagImportacao tag = this.tags.tag(coluna);
		if (tag == null) {
			throw new ImportacaoException(coluna, linha, "A coluna não corresponde a uma coluna de rota válida.");
		}
		this.rota.incluiValor(new ValorImportacao(linha, coluna, valor, tag));
	}

	@Override
	public void reseta() {
		this.rota = null;
	}

	@Override
	public boolean estahCompleto() {
		return this.rota != null && this.rota.estahCompleta();
	}

	@Override
	public void validaTags() {
		if (!this.tags.atendeRequisitosMinimos()) {
			TagImportacao primeira = tags.primeira();
			throw new ImportacaoExceptionBloqueante(primeira.coluna(), primeira.linha(), "A configuração de rota informada não é válida.");
		}
		
	}

	@Override
	public int linha() {
		TagImportacao tag = this.tags.primeira();
		return tag.linha();
	}

	@Override
	public int coluna() {
		TagImportacao tag = this.tags.primeira();
		return tag.coluna();
	}
	
	

}
