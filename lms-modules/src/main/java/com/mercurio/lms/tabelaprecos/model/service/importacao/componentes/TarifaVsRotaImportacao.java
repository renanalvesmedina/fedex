package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;

public class TarifaVsRotaImportacao implements ChaveProgressao {

	private Set<RotaImportacao> rotasImportacao = new TreeSet<RotaImportacao>();
	private CodigoTarifaImportacao tarifaImportacao;

	public TarifaVsRotaImportacao(CodigoTarifaImportacao tarifa) {
		this.tarifaImportacao = tarifa;
	}

	public void incluiRota(RotaImportacao rota) {
		this.rotasImportacao.add(rota);
	}

	public void incluiTarifa(CodigoTarifaImportacao tarifa) {
		this.tarifaImportacao = tarifa;
	}

	@Override
	public boolean valida() {
		if (CollectionUtils.isEmpty(this.rotasImportacao) && this.tarifaImportacao != null) {
			throw new ImportacaoException(this.tarifaImportacao.coluna(), this.tarifaImportacao.linha(), "Para importar tarifas e rotas, uma rota válida deve ser informada.");
		}
		if (CollectionUtils.isNotEmpty(this.rotasImportacao) && this.tarifaImportacao == null) {
			RotaImportacao rota = buscaPrimeiraRota();
			throw new ImportacaoException(rota.coluna(), rota.linha(), "Para importar tarifas e rotas, uma tarifa válida deve ser informada.");
		}
		return CollectionUtils.isNotEmpty(this.rotasImportacao) && this.tarifaImportacao != null;
	}

	private RotaImportacao buscaPrimeiraRota() {
		if (CollectionUtils.isEmpty(this.rotasImportacao)){
			return null;
		}
		return this.rotasImportacao.iterator().next();
	}

	@Override
	public boolean estahCompleta() {
		return valida();
	}

	@Override
	public int linha() {
		RotaImportacao rota = this.buscaPrimeiraRota();
		int linhaRota = rota == null ? Integer.MAX_VALUE : rota.linha();
		int linhaTarifa = this.tarifaImportacao == null ? Integer.MAX_VALUE : this.tarifaImportacao.linha();
		return Math.min(linhaRota, linhaTarifa);
	}
	
	@Override
	public int coluna() {
		RotaImportacao rota = this.buscaPrimeiraRota();
		int colunaRota = rota == null ? Integer.MAX_VALUE : rota.coluna();
		int colunaTarifa = this.tarifaImportacao == null ? Integer.MAX_VALUE : this.tarifaImportacao.coluna();
		return Math.min(colunaRota, colunaTarifa);
	}

	@Override
	public TipoChaveProgressao tipo() {
		return TipoChaveProgressao.TARIFA_X_ROTA;
	}

	public List<ChaveProgressao> rotas() {
		return Collections.unmodifiableList(new ArrayList<ChaveProgressao>(this.rotasImportacao));
	}

	public ChaveProgressao tarifa() {
		return this.tarifaImportacao;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!TarifaVsRotaImportacao.class.isInstance(o)) {
			return false;
		}
		TarifaVsRotaImportacao outro = (TarifaVsRotaImportacao) o;
		return new EqualsBuilder().append(this.tarifaImportacao, outro.tarifaImportacao).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.tarifaImportacao).toHashCode();
	}

	@Override
	public int compareTo(ChaveProgressao o) {
		if (!TarifaVsRotaImportacao.class.isAssignableFrom(o.getClass())) {
			return -1;
		}
		TarifaVsRotaImportacao tarifaXRota = (TarifaVsRotaImportacao) o;
		return this.hashCode() - tarifaXRota.hashCode();
	}

	public void incluiRotas(Iterator<RotaImportacao> iterator) {
		if (iterator == null) {
			return;
		}
		while (iterator.hasNext()) {
			this.incluiRota(iterator.next());
		}
		
	}

}
