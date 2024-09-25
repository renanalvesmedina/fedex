package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.CodigoTarifaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.RotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TarifaVsRotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.Observador;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.Observavel;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

public class AgenteTarifaVsRota implements AgenteChaveProgressao, Observador {

	private TagImportacao tag;
	private RotaImportacao rotaAtual;
	private Set<RotaImportacao> rotas = new TreeSet<RotaImportacao>();
	private CodigoTarifaImportacao tarifa;
	private TarifaVsRotaImportacao tarifaVsRota;

	@Override
	public boolean respondePor(String tag) {
		return TagsImportacaoTabelaPreco.TAG_TARIFA_X_ROTA.equals(tag);
	}

	@Override
	public void informaTag(TagImportacao tag) {
		this.tag = tag;
	}

	@Override
	public void atualiza(Observavel observavel) {
		if (observavel == null) { 
			return;
		}
		if (!AgenteObservavel.class.isInstance(observavel)) {
			return;
		}
		AgenteObservavel agente = (AgenteObservavel) observavel;
		if (TipoAgente.ROTA.equals(agente.tipo())) {
			this.rotaAtual = (RotaImportacao) agente.chave();
		}
		if (TipoAgente.TARIFA.equals(agente.tipo())) {
			CodigoTarifaImportacao codigoAtual = (CodigoTarifaImportacao) agente.chave();
			if (this.tarifa != null && !this.tarifa.equals(codigoAtual)) {
				this.reinicia();
			}
			this.tarifa = codigoAtual;
		}
		this.incluiRota();
	}
	
	private void incluiRota() {
		if (this.rotaAtual == null) {
			return;
		}
		RotaImportacao rotaJahInformada = this.buscaRota(rotaAtual);
		if (rotaJahInformada != null && rotaJahInformada.linha() != rotaAtual.linha()) {
			throw new ImportacaoException(rotaAtual.coluna(), rotaAtual.linha(), String.format("A rota configurada nessa linha já foi informada na linha %s.", rotaJahInformada.linha() + 1));
		}
		this.rotas.add(rotaAtual);
	}

	private RotaImportacao buscaRota(final RotaImportacao novaRota) {
		return (RotaImportacao) CollectionUtils.find(this.rotas, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				RotaImportacao rota = (RotaImportacao) object;
				return rota.equals(novaRota);
			}
		});
	}

	private void reinicia() {
		this.tarifaVsRota = null;
		this.rotas = new TreeSet<RotaImportacao>();
		this.tarifa = null;
	}

	@Override
	public TipoAgente tipo() {
		return TipoAgente.TARIFA_X_ROTA;
	}

	@Override
	public ChaveProgressao chave() {
		if (this.tarifaVsRota == null) {
			this.tarifaVsRota = new TarifaVsRotaImportacao(this.tarifa);
		}
		if (CollectionUtils.isNotEmpty(this.rotas)) {
			this.tarifaVsRota.incluiRotas(this.rotas.iterator());
		}
		return this.tarifaVsRota;
	}

	@Override
	public void reseta() {
		this.rotaAtual = null;
	}

	@Override
	public boolean estahCompleto() {
		return this.rotaAtual != null && this.rotaAtual.estahCompleta() && this.tarifa != null && this.tarifa.estahCompleta();
	}

	@Override
	public void incluiValor(int linha, int coluna, String valor) {
		//Não deve ser implementado.
	}

	@Override
	public void validaTags() {
		if (this.tag == null || StringUtils.isBlank(this.tag.tag())) {
			throw new ImportacaoException(0, 0, String.format("A tag '%s' era esperada nessa planilha.", TagsImportacaoTabelaPreco.TAG_TARIFA_X_ROTA));
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
	
	@Override
	public void encerraLinha() {
		//não faz nada
	}


}
