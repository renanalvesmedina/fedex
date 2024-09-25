package com.mercurio.lms.ppd.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdGrupoAtendimento;
import com.mercurio.lms.ppd.model.service.PpdGrupoAtendimentoService;

public class ManterGruposAtendimentoAction {
	PpdGrupoAtendimentoService grupoAtendimentoService;

	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = grupoAtendimentoService.findById(id).getMapped();
    	return bean;
	}
	
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = grupoAtendimentoService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdGrupoAtendimento> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(int i=0;i<list.size();i++) {
			PpdGrupoAtendimento item = list.get(i);
			retorno.add(item.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
	}
	
	 public Map<String, Object> store(Map<String, Object> bean) {
		 PpdGrupoAtendimento pojo = new PpdGrupoAtendimento();		 
		 pojo.setDsGrupoAtendimento((String)bean.get("dsGrupoAtendimento"));
		 grupoAtendimentoService.store(pojo);
		 
		 Map<String,Object> retorno = new HashMap<String, Object>();
		 retorno.put("idGrupoAtendimento", pojo.getIdGrupoAtendimento());
		 
		 return retorno;
	 }
	 
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		grupoAtendimentoService.removeByIds(ids);
	}
	
	public void removeById(Long id) {
		grupoAtendimentoService.removeById(id);
	}

	// Set das classes de serviço
	public void setStatusReciboService(PpdGrupoAtendimentoService grupoAtendimentoService) {
		this.grupoAtendimentoService = grupoAtendimentoService;
	}	
	
	public List<Map<String, Object>> findLookup(Map<String, Object> criteria) {
    	return grupoAtendimentoService.findLookup(MapUtils.getString(criteria, "dsGrupoAtendimento"));
    }
	
}
