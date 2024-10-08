package com.mercurio.lms.municipios.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.gerarFluxoCargaFilialNovaAction"
 */
public class GerarFluxoCargaFilialNovaAction extends CrudAction {
	private FilialService filialService;
	private ServicoService servicoService;

	public List findLookupFilial(Map criteria) {
		return filialService.findLookupFilial(criteria);
	}

	public List findServico(Map criteria) {
		return servicoService.find(criteria);
	}

	public void removeById(java.lang.Long id) {
		getFluxoFilialService().removeById(id);
	}

	/*
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getFluxoFilialService().removeByIds(ids);
	}

	public FluxoFilial findById(java.lang.Long id) {
		return getFluxoFilialService().findById(id);
	}

	public Integer getRowCountFluxoInicialOrigem(TypedFlatMap tfm) {
		if ("O".equals(tfm.getString("tpGeracao")))
			return getFluxoFilialService().getRowCountFluxoInicialOrigem(tfm);
		return getFluxoFilialService().getRowCountFluxoInicialDestino(tfm);
	}

	public ResultSetPage findPagindatedFluxoInicialOrigem(TypedFlatMap tfm) {
		if ("O".equals(tfm.getString("tpGeracao")))
			return getFluxoFilialService().findPagindatedFluxoInicialOrigem(tfm);
		return getFluxoFilialService().findPagindatedFluxoInicialDestino(tfm);
	}

	public Map findByIdFilial(Long idFilial) {
		Filial bean = filialService.findById(idFilial);
		TypedFlatMap result = new TypedFlatMap();
		result.put("siglaNomeFilial",bean.getSiglaNomeFilial());
		result.put("idFilial",bean.getIdFilial());
		result.put("sgFilial",bean.getSgFilial());
		result.put("pessoa.Fantasia",bean.getPessoa().getNmFantasia());
		return result;
	}

	public Map findDistancia(TypedFlatMap map) {
		Map mapRetorno = null;

		FluxoFilial fluxoFilial = getFluxoFilialService().findFluxoFilial(
			map.getLong("idFilialOrigem"),
			map.getLong("idFilialDestino"),
			JTDateTimeUtils.getDataAtual(),
			map.getLong("idServico")
		);

		if(fluxoFilial != null) {
			mapRetorno = new HashMap<String, Integer>();
			mapRetorno.put("nrDistancia", fluxoFilial.getNrDistancia());
		}

		return mapRetorno;
	}

	public Serializable storeMap(TypedFlatMap parameters) {
		FluxoFilial fluxoFilial = new FluxoFilial();

		Long idServico = parameters.getLong("servico.idServico");
		if (idServico != null) {
			Servico servico = new Servico();
			servico.setIdServico(idServico);
		}

		Long idFilialOrigem = parameters.getLong("filialByIdFilialOrigem.idFilial");
		if(idFilialOrigem != null) {
			Filial filialOrigem = new Filial();
			filialOrigem.setIdFilial(idFilialOrigem);
			fluxoFilial.setFilialByIdFilialOrigem(filialOrigem);
		}

		Long idFilialDestino = parameters.getLong("filialByIdFilialDestino.idFilial");
		if(idFilialDestino != null) {
			Filial filialDestino = new Filial();
			filialDestino.setIdFilial(idFilialDestino);
			fluxoFilial.setFilialByIdFilialDestino(filialDestino);
		}

		Long idFilialReembarcadora = parameters.getLong("filialByIdFilialReembarcadora.idFilial");
		if(idFilialReembarcadora != null) {
			Filial filialReembarcadora = new Filial();
			filialReembarcadora.setIdFilial(idFilialReembarcadora);
			fluxoFilial.setFilialByIdFilialReembarcadora(filialReembarcadora);
		}

		fluxoFilial.setBlDomingo(parameters.getBoolean("blDomingo"));
		fluxoFilial.setBlSegunda(parameters.getBoolean("blSegunda"));
		fluxoFilial.setBlTerca(parameters.getBoolean("blTerca"));
		fluxoFilial.setBlQuarta(parameters.getBoolean("blQuarta"));
		fluxoFilial.setBlQuinta(parameters.getBoolean("blQuinta"));
		fluxoFilial.setBlSexta(parameters.getBoolean("blSexta"));
		fluxoFilial.setBlSabado(parameters.getBoolean("blSabado"));

		fluxoFilial.setNrPrazoView(parameters.getInteger("nrPrazoView"));
		fluxoFilial.setNrDistancia(parameters.getInteger("nrDistancia"));
		fluxoFilial.setNrGrauDificuldade(parameters.getInteger("nrGrauDificuldade"));
		fluxoFilial.setDtVigenciaInicial(parameters.getYearMonthDay("dtVigenciaInicial"));
		fluxoFilial.setDtVigenciaFinal(parameters.getYearMonthDay("dtVigenciaFinal"));

		getFluxoFilialService().store(fluxoFilial);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idFluxoFilial", fluxoFilial.getIdFluxoFilial());
		retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(fluxoFilial));

		return retorno;
	}

	public FluxoFilial findFluxoReembarqueToMap(TypedFlatMap map) {
		return getFluxoFilialService().findFluxoReembarqueToMap(map);
	}

	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.defaultService = fluxoFilialService;
	}
	private final FluxoFilialService getFluxoFilialService() {
		return (FluxoFilialService) this.defaultService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
}
