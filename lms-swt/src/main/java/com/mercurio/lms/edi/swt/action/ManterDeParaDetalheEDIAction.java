package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.edi.model.DeParaDetalheEDI;
import com.mercurio.lms.edi.model.DeParaEDI;
import com.mercurio.lms.edi.model.service.DeParaDetalheEDIService;

public class ManterDeParaDetalheEDIAction {
	
	private DeParaDetalheEDIService deParaDetalheEDIService; 

	
	/**
	 * Obtem o DeParaDetalheEDI através do ID
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = deParaDetalheEDIService.findById(id).getMapped();
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
		
		DeParaDetalheEDI pojo = new DeParaDetalheEDI();
		pojo.setIdDeParaEDIDetalhe(map.getLong("idDeParaEDIDetalhe"));
		pojo.setDe(map.getString("de"));
		pojo.setPara(map.getString("para"));
		
		DeParaEDI deParaEDI = new DeParaEDI();
		deParaEDI.setIdDeParaEDI(map.getLong("idDeParaEDI"));
		pojo.setDeParaEDI(deParaEDI);
		
		deParaDetalheEDIService.store(pojo);
		
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idDeParaEDIDetalhe", pojo.getIdDeParaEDIDetalhe());
		
		return bean;
	}
		
	/**
	 * Retorna a lista de registros para a grid
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		
		ResultSetPage rsp = deParaDetalheEDIService.findPaginated(new PaginatedQuery(criteria));					
		
		List<DeParaDetalheEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(DeParaDetalheEDI depara : list){
			retorno.add(depara.getMapped());
		}
				
		rsp.setList(retorno);
		return rsp;		
	}	
	
	/**
	 * Remove uma lista de ManterDeParaDetalheEDI
	 * 
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		deParaDetalheEDIService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * 
	 * @param id
	 */
	public void removeById(Long id) {
		deParaDetalheEDIService.removeById(id);
	}		
	
	public void setDeParaDetalheEDIService(DeParaDetalheEDIService deParaDetalheEDIService) {
		this.deParaDetalheEDIService = deParaDetalheEDIService;
	}
}