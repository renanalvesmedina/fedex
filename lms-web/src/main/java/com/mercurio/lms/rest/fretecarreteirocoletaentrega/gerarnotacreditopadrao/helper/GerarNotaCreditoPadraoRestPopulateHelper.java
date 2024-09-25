package com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.helper;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.dto.GerarNotaCreditoPadraoFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.dto.GerarNotaCreditoPadraoRestDTO;
import com.mercurio.lms.util.FormatUtils;

public class GerarNotaCreditoPadraoRestPopulateHelper {
	
	/**
	 * Default private constructor
	 */
	private GerarNotaCreditoPadraoRestPopulateHelper(){
		
	}
	
	/**
	 * 
	 * @param criteria
	 * @param filter
	 * 
	 * @return TypedFlatMap
	 */
	public static TypedFlatMap getFilterMap(TypedFlatMap criteria, GerarNotaCreditoPadraoFilterDTO filter){		
		criteria.put("dhSaidaColetaEntrega", filter.getDhSaidaColetaEntrega());
		
		getTpGeracaoNotaCredito(criteria, filter);
		getFilialFilter(criteria, filter);	
		getProprietarioFilter(criteria, filter);		
		getMeioTransporteFilter(criteria, filter);
		getControleCargaFilter(criteria, filter);
		
		return criteria;
	}
	
	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTpGeracaoNotaCredito(TypedFlatMap criteria,
			GerarNotaCreditoPadraoFilterDTO filter) {
		if(filter.getTpGerarNotaCredito() != null){
			criteria.put("tpGeracaoNotaCredito", filter.getTpGerarNotaCredito().getValue());
		}
	}
	
	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getFilialFilter(TypedFlatMap criteria,
			GerarNotaCreditoPadraoFilterDTO filter) {
		if(filter.getFilial() != null){
			criteria.put("idFilial", filter.getFilial().getIdFilial());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getProprietarioFilter(TypedFlatMap criteria,
			GerarNotaCreditoPadraoFilterDTO filter) {
		if(filter.getProprietario() != null){
			criteria.put("idProprietario", filter.getProprietario().getIdProprietario());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getMeioTransporteFilter(TypedFlatMap criteria,
			GerarNotaCreditoPadraoFilterDTO filter) {
		if(filter.getMeioTransporte() != null){
			criteria.put("idMeioTransporte", filter.getMeioTransporte().getIdMeioTransporte());
		}
	}
	
	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getControleCargaFilter(TypedFlatMap criteria,
			GerarNotaCreditoPadraoFilterDTO filter) {
		if(filter.getControleCarga() != null){
			criteria.put("idControleCarga", filter.getControleCarga().getIdControleCarga());
		}
	}
	
	/**
	 * 
	 * @param resultSetPage
	 * 
	 * @return List<GerarNotaCreditoPadraoRestDTO>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<GerarNotaCreditoPadraoRestDTO> getListGerarNotaCreditoPadraoRestDTO(ResultSetPage resultSetPage){
		List<GerarNotaCreditoPadraoRestDTO> result = new ArrayList<GerarNotaCreditoPadraoRestDTO>();
		
		List<Object[]> list = resultSetPage.getList();
		
		for (Object[] item : list) {
			GerarNotaCreditoPadraoRestDTO gerarNotaCreditoPadrao = new GerarNotaCreditoPadraoRestDTO();			
			gerarNotaCreditoPadrao.setIdControleCarga(Long.valueOf(item[0].toString()));
			gerarNotaCreditoPadrao.setNrControleCarga(Long.valueOf(item[1].toString()));
			gerarNotaCreditoPadrao.setSgFilialControleCarga(String.valueOf(item[2]));
			gerarNotaCreditoPadrao.setNmPessoaProprietario(String.valueOf(item[3]));
			gerarNotaCreditoPadrao.setTpIdentificacaoProprietario(String.valueOf(item[4]));
			gerarNotaCreditoPadrao.setNrIdentificacaoProprietario(String.valueOf(item[5]));
						
			gerarNotaCreditoPadrao.setTpIdentificacaoProprietarioFormatado(
					FormatUtils.formatIdentificacao(
							gerarNotaCreditoPadrao.getTpIdentificacaoProprietario(), 
							gerarNotaCreditoPadrao.getNrIdentificacaoProprietario()));
			
			/*
			 * Necessário informar o ID para o framework.
			 */
			gerarNotaCreditoPadrao.setId(gerarNotaCreditoPadrao.getIdControleCarga());
			
			result.add(gerarNotaCreditoPadrao);
		}
		
		return result;
	}
}