package com.mercurio.lms.ppd.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.ppd.model.PpdAtendimentoFilial;
import com.mercurio.lms.ppd.model.PpdGrupoAtendimento;
import com.mercurio.lms.ppd.model.service.PpdAtendimentoFilialService;

public class ManterAtendimentoFiliaisAction {
	private PpdAtendimentoFilialService atendimentoFilialService;
	

	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = atendimentoFilialService.findById(id).getMapped();
    	return bean;
	}
	
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = atendimentoFilialService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdAtendimentoFilial> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(int i=0;i<list.size();i++) {
			PpdAtendimentoFilial item = list.get(i);
			retorno.add(item.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
	}

	public Map<String, Object> store(Map<String, Object> bean) {
		Filial filial = new Filial();
		filial.setIdFilial((Long) bean.get("idFilial"));			
		
		PpdGrupoAtendimento grupo = new PpdGrupoAtendimento();		
		grupo.setIdGrupoAtendimento((Long) bean.get("idGrupoAtendimento"));
		
		PpdAtendimentoFilial atendimentoFilial = new PpdAtendimentoFilial();
		atendimentoFilial.setFilial(filial);
		atendimentoFilial.setGrupoAtendimento(grupo);
		atendimentoFilialService.store(atendimentoFilial);
		
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idAtendimentoFilial", atendimentoFilial.getIdAtendimentoFilial());
		
		return retorno;
	}
	 
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		atendimentoFilialService.removeByIds(ids);
	}
	
	public void removeById(Long id) {
		atendimentoFilialService.removeById(id);
	}
	
	// Set das classes de serviço
	public void setStatusReciboService(PpdAtendimentoFilialService atendimentoFilialService) {
		this.atendimentoFilialService = atendimentoFilialService;
	}
}
