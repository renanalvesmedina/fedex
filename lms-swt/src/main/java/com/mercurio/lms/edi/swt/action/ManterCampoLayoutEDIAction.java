package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.CampoLayoutEDI;
import com.mercurio.lms.edi.model.service.CampoLayoutEDIService;

public class ManterCampoLayoutEDIAction {

	private CampoLayoutEDIService campoLayoutEDIService;
	
	/**
	 * Obtem o CampoLayoutEDI através do ID
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = campoLayoutEDIService.findById(id).getMapped();
    	return bean;
	}	
		
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = campoLayoutEDIService.findPaginated(new PaginatedQuery(criteria));					
		
		List<CampoLayoutEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(CampoLayoutEDI campo : list){
			retorno.add(campo.getMapped());				
		}		
		rsp.setList(retorno);
		
		return rsp;		
	}	
		
	/**
	 * Persiste o CampoLayoutEDI
	 * 
	 * @param bean
	 * @return
	 */
	public Map<String, Object> store(Map<String, Object> bean) {
		 
		 CampoLayoutEDI pojo = new CampoLayoutEDI();
		 /*Id Campo*/
		 pojo.setIdCampo((Long)bean.get("idCampo"));
		 /*Nome do campo*/
		 pojo.setNomeCampo((String)bean.get("nomeCampo"));
		 /*Descrição*/
		 pojo.setDescricaoCampo((String)bean.get("descricaoCampo"));
		 /*Campo Tabela*/
		 pojo.setCampoTabela((String)bean.get("campoTabela"));
		 /*Nome Complemento*/
		 pojo.setNmComplemento((String)bean.get("nmComplemento"));
		 /*Obrigatório*/
		 pojo.setDmObrigatorio(new DomainValue((String)bean.get("dmObrigatorio")));
		 /*Tipo de Para*/
		 if(bean.get("dmTipoDePara") != null){
			 pojo.setDmTipoDePara(new DomainValue((String)bean.get("dmTipoDePara")));
		 }
		 		 		 		 
		 /*Salva o Campo Layout EDI*/
		 campoLayoutEDIService.store(pojo);
			 
		 Map<String,Object> retorno = new HashMap<String, Object>();
		 retorno.put("idCampo", pojo.getIdCampo());
			 
		 return retorno;
	}	
	
	/**
	 * Remove uma lista de CampoLayoutEDI
	 * 
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		campoLayoutEDIService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * 
	 * @param id
	 */
	public void removeById(Long id) {
		campoLayoutEDIService.removeById(id);
	}

	/**
	 * Lookup CampoLayoutEDI
	 * 
	 * @return List
	 */
	public List findLookup(Map criteria){
		List lista = campoLayoutEDIService.findLookup(criteria);		
		if (!lista.isEmpty() && lista.size() == 1) {
			CampoLayoutEDI layout = (CampoLayoutEDI)lista.get(0);
			Map map = new HashMap();
			map.put("idCampo", layout.getIdCampo());
			map.put("nomeCampo", layout.getNomeCampo());
			lista.add(map);
			lista.remove(layout);
		}
		return lista;		
	}		

	public CampoLayoutEDIService getCampoLayoutEDIService() {
		return campoLayoutEDIService;
	}

	public void setCampoLayoutEDIService(CampoLayoutEDIService campoLayoutEDIService) {
		this.campoLayoutEDIService = campoLayoutEDIService;
	}	
}