package com.mercurio.lms.tributos.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.EmbasamentoLegalIcms;
import com.mercurio.lms.tributos.model.ExcecaoICMSIntegrantesContribuintes;
import com.mercurio.lms.tributos.model.service.EmbasamentoLegalIcmsService;
import com.mercurio.lms.tributos.model.service.ExcecaoICMSIntegranteContribuintesService;
import com.mercurio.lms.util.session.SessionUtils;


public class ManterExcecaoIcmsContribuintesAction {

	private UnidadeFederativaService unidadeFederativaService;
	private EmbasamentoLegalIcmsService embasamentoService;
	private ExcecaoICMSIntegranteContribuintesService excecaoICMSIntegranteContribuintesService;
	
	public List<Map<String, Object>> findEmbasamentoLookup(TypedFlatMap criteria) {
		return embasamentoService.findEmbasamentoLookup(createEmbasamentoFindCriteria(criteria));
	}
	
	private TypedFlatMap createEmbasamentoFindCriteria(Map criteria) {
		TypedFlatMap mapCriteria = new TypedFlatMap();
		mapCriteria.put("_currentPage", criteria.get("_currentPage"));
    	mapCriteria.put("_pageSize", criteria.get("_pageSize"));
    	mapCriteria.put("_order", criteria.get("_order"));

    	if(criteria.get("idEmbasamento") != null) {
    		mapCriteria.put("idEmbasamento", criteria.get("idEmbasamento"));
    	}
    		
    	mapCriteria.put("unidadeFederativaOrigem.sgUnidadeFederativa", criteria.get("sgUnidadeFederativa"));
    	return mapCriteria;
	} 
		
	public void removeById(Long id) {
		excecaoICMSIntegranteContribuintesService.removeById(id);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		excecaoICMSIntegranteContribuintesService.removeByIds(ids);
	}
	
	public void store(TypedFlatMap data) {
		ExcecaoICMSIntegrantesContribuintes bean = new ExcecaoICMSIntegrantesContribuintes().fillByMap(data);
		excecaoICMSIntegranteContribuintesService.store(bean);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage resultPage = excecaoICMSIntegranteContribuintesService.findPaginated(createFindCriteria(criteria));
		List<TypedFlatMap> listResult = new ArrayList<TypedFlatMap>();

		for(int i=0; i< resultPage.getList().size(); i++) {
			ExcecaoICMSIntegrantesContribuintes element = (ExcecaoICMSIntegrantesContribuintes) resultPage.getList().get(i);
			TypedFlatMap map  = new TypedFlatMap();
			map.put("id", 							element.getId());
			map.put("periodoFinal", 				element.getDtVigenciaFinal());
			map.put("periodoInicial", 				element.getDtVigenciaInicial());
			map.put("embasamento", 					getEmbasamentoLegalIcms(element.getEmbasamentoLegalIcms()));
			map.put("sgUnidadeFederativaOrigem",	getSiglaUnidadeFederativa(element.getUnidadeFederativaOrigem()));
			map.put("sgUnidadeFederativaDestino", 	getSiglaUnidadeFederativa(element.getUnidadeFederativaDestino()));
			map.put("tpFrete", 						getDomainValue(element.getTpFrete()));
			map.put("integranteFrete",				getDomainValue(element.getTpIntegranteFrete()));
			listResult.add(map);
		}

		resultPage.setList(listResult);
    	return resultPage;
	}
	
	private TypedFlatMap createFindCriteria(Map criteria) {
		TypedFlatMap mapCriteria = new TypedFlatMap();
		mapCriteria.put("_currentPage", criteria.get("_currentPage"));
    	mapCriteria.put("_pageSize", criteria.get("_pageSize"));
    	mapCriteria.put("_order", criteria.get("_order"));

    	mapCriteria.put("id", criteria.get("id"));
    	mapCriteria.put("unidadeFederativaOrigem.idUnidadeFederativa", criteria.get("idUnidadeFederativaOrigem"));
    	mapCriteria.put("unidadeFederativaDestino.idUnidadeFederativa", criteria.get("idUnidadeFederativaDestino"));
    	mapCriteria.put("tpFrete", criteria.get("tpFrete"));
    	mapCriteria.put("tpIntegranteFrete", criteria.get("integranteFrete"));
    	mapCriteria.put("embasamentoLegalIcms.idEmbasamento", criteria.get("idEmbasamento"));
    	mapCriteria.put("dtVigenciaInicial", criteria.get("periodoInicial"));
    	mapCriteria.put("dtVigenciaFinal", criteria.get("periodoFinal"));
    	return mapCriteria;
	}  	
	
	public int findRowCount(TypedFlatMap criteria){
		return excecaoICMSIntegranteContribuintesService.getRowCount(createFindCriteria(criteria));
	}
	
	public Map findById(Long id) {
		ExcecaoICMSIntegrantesContribuintes element = excecaoICMSIntegranteContribuintesService.findById(id); 
		TypedFlatMap map  = new TypedFlatMap();
		map.put("id", 							element.getId());
		map.put("periodoFinal", 				element.getDtVigenciaFinal());
		map.put("periodoInicial", 				element.getDtVigenciaInicial());
		map.put("idEmbasamento",				getIdEmbasamentoLegalIcms(element.getEmbasamentoLegalIcms()));
		map.put("descEmbasamento",				getEmbasamentoLegalIcms(element.getEmbasamentoLegalIcms()));
		map.put("sgUnidadeFederativa",			getUFEmbasamentoLegalIcms(element.getEmbasamentoLegalIcms()));
		map.put("idUnidadeFederativaOrigem",	getIdUnidadeFederativa(element.getUnidadeFederativaOrigem()));
		map.put("idUnidadeFederativaDestino", 	getIdUnidadeFederativa(element.getUnidadeFederativaDestino()));
		map.put("idTpFrete", 					getValueDomainValue(element.getTpFrete()));
		map.put("idIntegranteFrete",			getValueDomainValue(element.getTpIntegranteFrete()));
	 	return map;
	}

	public List findUnidadeFederativa(TypedFlatMap criteria, String idUF) {
		criteria.put("idPais", SessionUtils.getPaisSessao().getIdPais());
		List listUF = unidadeFederativaService.findComboAtivo(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = listUF.iterator(); iter.hasNext();) {
			Map mapUF = (HashMap)iter.next();
			Map include = new HashMap();
			include.put(idUF, mapUF.get("idUnidadeFederativa"));
			include.put("siglaDescricao", mapUF.get("siglaDescricao"));
			include.put("sgUnidadeFederativa", mapUF.get("sgUnidadeFederativa"));
			retorno.add(include);
		}
    	return retorno;
	}
	
	public List findUnidadeFederativaOrigem(TypedFlatMap criteria) {
		return findUnidadeFederativa(criteria, "idUnidadeFederativaOrigem");
	}
	
	public List findUnidadeFederativaDestino(TypedFlatMap criteria) {
		return findUnidadeFederativa(criteria, "idUnidadeFederativaDestino");
	}
	
	public String getEmbasamentoLegalIcms(EmbasamentoLegalIcms embasamento){
		return embasamento == null ? "" : embasamento.getDsEmbLegalResumido();
	}
	
	public String getUFEmbasamentoLegalIcms(EmbasamentoLegalIcms embasamento){
		return embasamento == null ? "" : embasamento.getUnidadeFederativaOrigem().getSgUnidadeFederativa();
	}
	
	public Long getIdEmbasamentoLegalIcms(EmbasamentoLegalIcms embasamento){
		return embasamento == null ? null : embasamento.getIdEmbasamento();
	}
	
	public String getSiglaUnidadeFederativa(UnidadeFederativa uf){
		return uf == null ? "" : uf.getSgUnidadeFederativa();
	}
	
	public Long getIdUnidadeFederativa(UnidadeFederativa uf){
		return uf == null ? null : uf.getIdUnidadeFederativa();
	}
	
	public String getDomainValue(DomainValue domain){
		return domain == null ? "" : domain.getDescriptionAsString();
	}
	
	public String getValueDomainValue(DomainValue domain){
		return domain == null ? "" : domain.getValue();
	}
	
	public void setEmbasamentoService(
			EmbasamentoLegalIcmsService embasamentoService) {
		this.embasamentoService = embasamentoService;
	}
	
	public void setExcecaoICMSIntegranteContribuintesService(ExcecaoICMSIntegranteContribuintesService excecaoICMSIntegranteContribuintesService) {
		this.excecaoICMSIntegranteContribuintesService = excecaoICMSIntegranteContribuintesService;
	}
	
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
}
