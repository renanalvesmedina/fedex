package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.fornecedores;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.CodigoTarifaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.FornecedorChave;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.TradutorChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.util.session.SessionKey;

public class FornecedorTarifaPreco extends FornecedorChave {

	private TarifaPrecoService servicoTarifaPreco;
	private static final String TP_SITUACAO_ATIVA = "A";
	private static final int CACHE_SIZE = 50;
	private static final Empresa EMPRESA = (Empresa) SessionContext.get(SessionKey.EMPRESA_KEY);
	
	private List<TarifaPreco> cacheTarifas;

	public FornecedorTarifaPreco(TarifaPrecoService servicoTarifaPreco) {
		this.servicoTarifaPreco = servicoTarifaPreco;
	}
	
	@Override
	protected TradutorChaveProgressao executaForneceChave(ChaveProgressao chaveProgressao) {
		CodigoTarifaImportacao tarifaImportacao = (CodigoTarifaImportacao) chaveProgressao;
		TarifaPreco tarifa = this.buscaTarifa(tarifaImportacao);
		if (tarifa == null) {
			tarifa = this.incluiTarifa(tarifaImportacao);
		}
		return new TradutorChaveProgressao(null, tarifa);
	}


	private TarifaPreco buscaTarifa(CodigoTarifaImportacao tarifaImportacao) {
		return this.servicoTarifaPreco.findByCdTarifaPreco(tarifaImportacao.valorString());
	}

	private TarifaPreco incluiTarifa(CodigoTarifaImportacao tarifaImportacao) {
		TarifaPreco tarifa = new TarifaPreco();
		tarifa.setCdTarifaPreco(tarifaImportacao.valorString());
		tarifa.setTpSituacao((DomainValue) ReflectionUtils.toObject(TP_SITUACAO_ATIVA, DomainValue.class));
		tarifa.setEmpresa(EMPRESA);
		this.processaTarifasNovas(tarifa);
		return tarifa;
	}
	
	private void processaTarifasNovas(TarifaPreco tarifaPreco){
		this.initTarifaCache();
		cacheTarifas.add(tarifaPreco);
		if(cacheTarifas.size() == CACHE_SIZE){
			this.servicoTarifaPreco.storeAll(cacheTarifas);
			cacheTarifas.clear();
		}
	}
	
	private void initTarifaCache(){
		if(cacheTarifas == null){
			cacheTarifas = new ArrayList<TarifaPreco>();
		}
	}

	@Override
	public void inicializa() {
		//intencionalmente deixado em branco pois não há necessidade de faze-lo
	}

	@Override
	public void persisteCache() {
		if(!CollectionUtils.isEmpty(cacheTarifas)){
			this.servicoTarifaPreco.storeAll(cacheTarifas);
			cacheTarifas.clear();
		}
	}
	
}
