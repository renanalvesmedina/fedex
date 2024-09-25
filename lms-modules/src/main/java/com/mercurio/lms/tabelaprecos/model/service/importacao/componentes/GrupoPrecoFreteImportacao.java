package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsPrecoFrete;

public class GrupoPrecoFreteImportacao implements ComponenteImportacao {

	private static final String FORMATO_TAG_PRINCIPAL_PRECO_FRETE = "[#%s_PRF_VLPRECOFRETE]";

	private String identificacao;
	private Set<PrecoFreteImportacao> itensPrecoFrete;
	private PrecoFreteImportacao precoFreteAtivo;
	private TagImportacao tagPrincipalGrupoPrecoFrete;

	public GrupoPrecoFreteImportacao(String identificacao, TagImportacao tagPrincipal) {
		this.identificacao = identificacao;
		this.tagPrincipalGrupoPrecoFrete = tagPrincipal;
	}

	public boolean incluiValor(ValorImportacao valor) {
		if (!TagsPrecoFrete.ehTagPrecoFrete(valor.tag())) {
			return false;
		}
		iniciaPrecoFrete();
		if (precoFreteAtivo.estahCompleto()) {
			throw new ImportacaoException(valor.coluna(), valor.linha(), String.format("O valor informado para '%s' já foi informado anteriormente.", valor.tag()));
		}
		precoFreteAtivo.incluiValor(valor);

		return true;
	}

	private void iniciaPrecoFrete() {
		if (this.precoFreteAtivo == null) {
			this.precoFreteAtivo = new PrecoFreteImportacao();
		}
	}

	private void iniciaItensPrecoFrete() {
		if (this.itensPrecoFrete == null) {
			this.itensPrecoFrete = new HashSet<PrecoFreteImportacao>();
		}
	}

	@Override
	public String valorTagPrincipal() {
		return String.format(FORMATO_TAG_PRINCIPAL_PRECO_FRETE, this.identificacao);
	}

	@Override
	public TipoComponente tipo() {
		return TipoComponente.PRECO_FRETE;
	}

	public void informaChave(ChaveProgressao chave) {
		iniciaPrecoFrete();
		PrecoFreteImportacao precoFreteInformado = this.buscaPrecoFretePelaChave(chave);
		if (precoFreteInformado != null) {
			if (precoFreteInformado.valorFrete().doubleValue() != this.precoFreteAtivo.valorFrete().doubleValue()) {
				throw new ImportacaoException(chave.coluna(), chave.linha(), 
						String.format("A chave '%s' informada nessa linha tem valores diferentes para essa faixa na linha %d.", chave.tipo().name(), precoFreteInformado.linha() + 1));
			}
			this.precoFreteAtivo = precoFreteInformado;
		} else {
			this.precoFreteAtivo.informaChave(chave);
		}
	}

	private PrecoFreteImportacao buscaPrecoFretePelaChave(final ChaveProgressao chave) {
		return (PrecoFreteImportacao) CollectionUtils.find(this.itensPrecoFrete, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return chave.equals(((PrecoFreteImportacao) object).chave());
			}
			
		});
	}

	public Set<PrecoFreteImportacao> valores() {
		if (CollectionUtils.isEmpty(this.itensPrecoFrete)) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(this.itensPrecoFrete);
	}

	@Override
	public TagImportacao tagPrincipal() {
		return this.tagPrincipalGrupoPrecoFrete;
	}

	public void novoRegistro() {
		if (this.precoFreteAtivo == null) {
			return;
		}
		this.iniciaItensPrecoFrete();
		this.itensPrecoFrete.add(precoFreteAtivo);
		precoFreteAtivo = null;
	}
	
	public void cancela() {
		this.precoFreteAtivo = null;
	}

}
