package com.mercurio.lms.ppd.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdNaturezaProduto;
import com.mercurio.lms.ppd.model.service.PpdNaturezaProdutoService;

public class ManterNaturezasProdutosAction {
	PpdNaturezaProdutoService naturezaProdutoService;

	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = naturezaProdutoService.findById(id).getMapped();
    	return bean;
	}
	
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = naturezaProdutoService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdNaturezaProduto> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(int i=0;i<list.size();i++) {
			PpdNaturezaProduto item = list.get(i);
			retorno.add(item.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
	}
	
	 public Map<String, Object> store(Map<String, Object> bean) {
		 PpdNaturezaProduto pojo = new PpdNaturezaProduto();		 
		 pojo.setDsNaturezaProduto((String)bean.get("dsNaturezaProduto"));
		 if(bean.get("idNaturezaProduto") != null)
			 pojo.setIdNaturezaProduto((Long) bean.get("idNaturezaProduto"));
		 
		 naturezaProdutoService.store(pojo);
		 
		 Map<String,Object> retorno = new HashMap<String, Object>();
		 retorno.put("idNaturezaProduto", pojo.getIdNaturezaProduto());
		 
		 return retorno;
	 }
	 
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		naturezaProdutoService.removeByIds(ids);
	}
	
	public void removeById(Long id) {
		naturezaProdutoService.removeById(id);
	}

	public List<Map<String, Object>> findLookup(Map<String, Object> criteria) {
    	return naturezaProdutoService.findLookup(MapUtils.getString(criteria, "dsNaturezaProduto"));
    }
	
	public List<Map<String,Object>> findCombo(Map<String,Object> criteria) {    	    	
    	return naturezaProdutoService.findCombo(criteria);	
	}
	
	// Set das classes de serviço
	public void setStatusReciboService(PpdNaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}		
}
