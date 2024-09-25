package com.mercurio.lms.rest.configuracoes.inscricaoestadual.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.rest.configuracoes.inscricaoestadual.dto.InscricaoEstadualDTO;
import com.mercurio.lms.rest.configuracoes.inscricaoestadual.dto.InscricaoEstadualFilterDTO;

public class InscricaoEstadualRestPopulateHelper {

	/**
	 * Default private constructor
	 */
	private InscricaoEstadualRestPopulateHelper(){
		
	}
	
	public static TypedFlatMap getFilterMap(TypedFlatMap criteria, InscricaoEstadualFilterDTO filter){
		criteria.put("idPessoa", filter.getIdPessoa());
		
		return criteria;
	}
	
	/**
	 * 
	 * @param resultSetPage
	 * @return List<DescontoRfcDTO>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<InscricaoEstadualDTO> getListInscricaoEstadualDTO(ResultSetPage resultSetPage){
		List<InscricaoEstadualDTO> result = new ArrayList<InscricaoEstadualDTO>();
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> inscricaoEstadual : list) {
			InscricaoEstadualDTO inscricaoEstadualDTO = new InscricaoEstadualDTO();
			inscricaoEstadualDTO.setIdInscricaoEstadual(MapUtils.getLong(inscricaoEstadual, "idInscricaoEstadual"));
			inscricaoEstadualDTO.setNrInscricaoEstadual(MapUtils.getString(inscricaoEstadual, "nrInscricaoEstadual"));
			inscricaoEstadualDTO.setSgUnidadeFederativa(MapUtils.getString(inscricaoEstadual, "sgUnidadeFederativa"));			
			inscricaoEstadualDTO.setSituacao((DomainValue) MapUtils.getObject(inscricaoEstadual, "tpSituacao"));			
			inscricaoEstadualDTO.setBlIndicadorPadrao(MapUtils.getBoolean(inscricaoEstadual, "blIndicadorPadrao"));
				
			/*
			 * Necessário informar o ID para o framework.
			 */
			inscricaoEstadualDTO.setId(inscricaoEstadualDTO.getIdInscricaoEstadual());
			
			result.add(inscricaoEstadualDTO);
		}
		
		return result;
	}
}