package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.fornecedores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoRotaService;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TarifaVsRotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.FornecedorChave;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.TradutorChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;

public class FornecedorTarifaVsRota extends FornecedorChave {
	
	private static final int CACHE_SIZE = 100;

	private TarifaPrecoRotaService servicoTarifaVsRota;
	private FornecedorRotaPreco fornecedorRota;
	private FornecedorTarifaPreco fornecedorTarifa;
	private TabelaPreco tabela;
	private List<TarifaPrecoRota> cacheTarifaPrecoRota;

	public FornecedorTarifaVsRota(TabelaPreco tabela, TarifaPrecoRotaService servicoTarifaVsRota, FornecedorRotaPreco fornecedorRota, FornecedorTarifaPreco fornecedorTarifa) {
		this.tabela = tabela;
		this.servicoTarifaVsRota = servicoTarifaVsRota;
		this.fornecedorRota = fornecedorRota;
		this.fornecedorTarifa = fornecedorTarifa;
	}
	
	@Override
	protected TradutorChaveProgressao executaForneceChave(ChaveProgressao chaveProgressao) {
		TarifaVsRotaImportacao tarifaXRota = (TarifaVsRotaImportacao) chaveProgressao;
		TarifaPreco tarifa = this.fornecedorTarifa.forneceChave(tarifaXRota.tarifa()).tarifaPreco();
		List<RotaPreco> rotas = this.buscaRotas(tarifaXRota.rotas());
		this.incluaTarifaVsRotas(tarifa, rotas);
		return new TradutorChaveProgressao(null, tarifa);
	}


	private void incluaTarifaVsRotas(TarifaPreco tarifa, List<RotaPreco> rotas) {
		List<TarifaPrecoRota> tarifasVsRotas = buscaTarifaVsRota(tarifa);
		List<RotaPreco> rotasNaoContidas = this.extraiRotasNaoContidasEm(rotas, tarifasVsRotas);
		if (CollectionUtils.isNotEmpty(rotasNaoContidas)) {
			List<TarifaPrecoRota> entidades = this.criaNovasEntidades(tarifa, rotasNaoContidas);
			processaTarifaPrecoRotaNovas(entidades);
		}
	}
	
	private void processaTarifaPrecoRotaNovas(List<TarifaPrecoRota> tarifaPrecoRotaList){
		this.initTarifaPrecoRotaCache();
		cacheTarifaPrecoRota.addAll(tarifaPrecoRotaList);
		if(cacheTarifaPrecoRota.size() == CACHE_SIZE){
			this.fornecedorRota.persisteCache();
			this.fornecedorTarifa.persisteCache();
			this.servicoTarifaVsRota.storeAll(cacheTarifaPrecoRota);
			cacheTarifaPrecoRota.clear();
		}
	}
	
	private void initTarifaPrecoRotaCache(){
		if(cacheTarifaPrecoRota == null){
			cacheTarifaPrecoRota = new ArrayList<TarifaPrecoRota>();
		}
	}

	private List<TarifaPrecoRota> criaNovasEntidades(TarifaPreco tarifa, List<RotaPreco> rotasNaoContidas) {
		List<TarifaPrecoRota> entidades = new ArrayList<TarifaPrecoRota>();
		for (RotaPreco rota : rotasNaoContidas) {
			TarifaPrecoRota tarifaVsRota = new TarifaPrecoRota();
			tarifaVsRota.setRotaPreco(rota);
			tarifaVsRota.setTarifaPreco(tarifa);
			tarifaVsRota.setTabelaPreco(tabela);
			entidades.add(tarifaVsRota);
		}
		return entidades;
	}

	private List<TarifaPrecoRota> buscaTarifaVsRota(TarifaPreco tarifa) {
		if(tarifa.getIdTarifaPreco() == null || tarifa.getIdTarifaPreco()  == 0L){
			return Collections.emptyList();
		}
		return this.servicoTarifaVsRota.findByTabelaETarifa(tabela.getIdTabelaPreco(), tarifa.getIdTarifaPreco());
	}

	private List<RotaPreco> extraiRotasNaoContidasEm(List<RotaPreco> rotas, List<TarifaPrecoRota> tarifasVsRotas) {
		List<RotaPreco> resultado = new ArrayList<RotaPreco>();
		for (RotaPreco rota: rotas) {
			if (!this.contemRota(tarifasVsRotas, rota)) {
				resultado.add(rota);
			}
		}
		return resultado;
	}

	private boolean contemRota(List<TarifaPrecoRota> tarifasVsRotas, final RotaPreco rota) {
		Object rotaEncontrada = CollectionUtils.find(tarifasVsRotas, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				TarifaPrecoRota corrente = (TarifaPrecoRota) object;
				return corrente.mesmaRota(rota);
			}
			
		});
		return rotaEncontrada != null;
	}

	private List<RotaPreco> buscaRotas(List<ChaveProgressao> rotas) {
		List<RotaPreco> resultado = new ArrayList<RotaPreco>();
		for (ChaveProgressao chaveProgressao : rotas) {
			resultado.add(this.fornecedorRota.forneceChave(chaveProgressao).rotaPreco());
		}
		return resultado;
	}

	@Override
	public void inicializa() {
		if (this.servicoTarifaVsRota == null) {
			throw new ADSMException("Serviço de tarifa vs rota não pode ser nulo.");
		}
		if (this.tabela == null) {
			throw new ADSMException("Tabela de preço não pode ser nula.");
		}
		this.servicoTarifaVsRota.removeByTabelaPreco(this.tabela.getIdTabelaPreco());
	}

	@Override
	public void persisteCache() {
		fornecedorRota.persisteCache();
		fornecedorTarifa.persisteCache();
		if(!CollectionUtils.isEmpty(cacheTarifaPrecoRota)){
			this.servicoTarifaVsRota.storeAll(cacheTarifaPrecoRota);
			cacheTarifaPrecoRota.clear();
		}
	}
}
