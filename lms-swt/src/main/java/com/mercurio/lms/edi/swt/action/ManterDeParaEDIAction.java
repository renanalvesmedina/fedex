package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.edi.model.DeParaEDI;
import com.mercurio.lms.edi.model.service.DeParaEDIService;

public class ManterDeParaEDIAction {
	
	private DeParaEDIService deParaEDIService;

	
	/**
	 * Obtem o DeParaEDI através do ID
	 * 
	 * @param id
	 * @return Map
	 */
	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = deParaEDIService.findById(id).getMapped();
    	return bean;
	}	
	
	/**
	 * Salva a entidade
	 * 
	 * @param bean
	 * @return Map
	 */
	public Map<String, Object> store(Map<String, Object> bean) {
		
		MapUtilsPlus map = new MapUtilsPlus(bean);
		
		DeParaEDI pojo = new DeParaEDI();
		pojo.setIdDeParaEDI(map.getLong("idDeParaEDI"));
		pojo.setNmDeParaEDI(map.getString("nmDeParaEDI"));
		
		deParaEDIService.store(pojo);
		
		bean.put("idDeParaEDI", pojo.getIdDeParaEDI());
		
		return bean;
	}
		
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		
		ResultSetPage rsp = deParaEDIService.findPaginated(new PaginatedQuery(criteria));					
		
		List<DeParaEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(DeParaEDI depara : list){
			retorno.add(depara.getMapped());
		}
				
		rsp.setList(retorno);
		return rsp;		
	}	
	
	/**
	 * Lookup ManterDeParaEDI
	 * 
	 * @return List
	 */
	public List findLookup(Map criteria){
		List lista = deParaEDIService.findLookup(criteria);		
		if (!lista.isEmpty() && lista.size() == 1) {
			DeParaEDI depara = (DeParaEDI)lista.get(0);
			Map map = new HashMap();
			map.put("idDeParaEDI", depara.getIdDeParaEDI());
			map.put("nmDeParaEDI", depara.getNmDeParaEDI());
			lista.add(map);
			lista.remove(depara);
		}
		return lista;		
	}
	
	/**
	 * Remove uma lista de ManterDeParaEDI
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		deParaEDIService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * @param id
	 */
	public void removeById(Long id) {
		deParaEDIService.removeById(id);
	}		
	
	public void setDeParaEDIService(DeParaEDIService deParaEDIService) {
		this.deParaEDIService = deParaEDIService;
	}

}
