package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.RegistroLayoutEDI;
import com.mercurio.lms.edi.model.service.RegistroLayoutEDIService;

public class ManterRegistroLayoutEDIAction {

	private RegistroLayoutEDIService registroLayoutEDIService;
	
	/**
	 * Obtem o LayoutEDI através do ID
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> findById(Long id) {    	
    	
		RegistroLayoutEDI registroLayoutEDI = registroLayoutEDIService.findById(id);
    			
		Map<String,Object> map = new HashMap<String, Object>();
		
		/*Id Registro Layout*/
		map.put("idRegistroLayoutEdi", registroLayoutEDI.getIdRegistroLayoutEdi());
		/*Registro*/
		map.put("identificador", registroLayoutEDI.getIdentificador());					
		/*Preenchimento*/
		map.put("preenchimento", registroLayoutEDI.getPreenchimento().getValue());
		/*N. Ocorrencias*/
		map.put("ocorrencias", registroLayoutEDI.getOcorrencias());
		/*Descricao*/
		map.put("descricao", registroLayoutEDI.getDescricao());
		/*Tamanho registro*/
		map.put("tamanhoRegistro", registroLayoutEDI.getTamanhoRegistro());
		/*Nome Identificador*/
		map.put("nomeIdentificador", registroLayoutEDI.getNomeIdentificador());		
		/*Ordem*/
		map.put("ordem", registroLayoutEDI.getOrdem());		
				
    	/*Registro Pai*/
		if(registroLayoutEDI.getIdRegistroPai() != null){
			RegistroLayoutEDI regPai = registroLayoutEDIService.findById(registroLayoutEDI.getIdRegistroPai());
			map.put("idRegistro", regPai.getIdRegistroLayoutEdi());			
			map.put("nrIdentificador", regPai.getIdentificador());			
		}    	
    	return map;
	}	
	
	
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = registroLayoutEDIService.findPaginated(new PaginatedQuery(criteria));					
		
		List<RegistroLayoutEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(RegistroLayoutEDI rel : list){	
			
			Map<String,Object> map = new HashMap<String, Object>();
			
			/*Id Registro Layout*/
			map.put("idRegistro", rel.getIdRegistroLayoutEdi());
			/*Registro*/
			map.put("nrIdentificador", rel.getIdentificador());					
			/*Descricao*/
			map.put("descricao", rel.getDescricao());			
			
			retorno.add(map);	
		}
		
		rsp.setList(retorno);
		return rsp;		
	}	
	
	
	/**
	 * Persiste o layoutEDI
	 * @param bean
	 * @return
	 */
	public Map<String, Object> store(Map<String, Object> bean) {
		 
		 RegistroLayoutEDI pojo = new RegistroLayoutEDI();
		 /*Id Registro*/
		 pojo.setIdRegistroLayoutEdi((Long)bean.get("idRegistroLayoutEdi"));
		 /*Numero Identificador*/
		 pojo.setIdentificador((String)bean.get("identificador"));
		 /*Prrenchimento*/
		 pojo.setPreenchimento(new DomainValue((String)bean.get("preenchimento")));
		 /*Ocorrencias*/
		 pojo.setOcorrencias((Integer)bean.get("ocorrencias"));
		 /*Descrição*/
		 pojo.setDescricao((String)bean.get("descricao"));
		 /*Tamanho Registro*/
		 pojo.setTamanhoRegistro((Integer)bean.get("tamanhoRegistro"));
		 /*Ordem*/
		 pojo.setOrdem((Integer)bean.get("ordem"));
		 /*Nome Identificador*/
		 pojo.setNomeIdentificador((String)bean.get("nomeIdentificador"));
		 
		 Map<String,Object> retorno = new HashMap<String, Object>();
		 
		 /*Registro Pai*/
		 if(bean.get("idRegistro") != null){
			 RegistroLayoutEDI regPai = registroLayoutEDIService.findById((Long)bean.get("idRegistro"));
			 pojo.setIdRegistroPai(regPai.getIdRegistroLayoutEdi());
			 retorno.put("idRegistro", regPai.getIdRegistroLayoutEdi());
			 retorno.put("nrIdentificador", regPai.getIdentificador());
		 }
		 		 
		 /*Salva o Registro Layout EDI*/
		 registroLayoutEDIService.store(pojo);
			 
		 retorno.put("idRegistroLayoutEdi", pojo.getIdRegistroLayoutEdi());
			 
		 return retorno;
	}	
	
	/**
	 * Remove uma lista de LayoutEDI
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		registroLayoutEDIService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * @param id
	 */
	public void removeById(Long id) {
		registroLayoutEDIService.removeById(id);
	}
	
	/**
	 * Lookup RegistroLayoutEDI 
	 * 
	 * @return List
	 */
	public List findLookup(Map criteria){
		
		String nrIdentificador = (String)criteria.get("nrIdentificador");
		
		List list = new ArrayList();
		if(StringUtils.isNotBlank(nrIdentificador)){
			List<RegistroLayoutEDI> lsRegistro = registroLayoutEDIService.findLookupRegistro(criteria);		
			if (lsRegistro != null && lsRegistro.size() == 1) {
				RegistroLayoutEDI layout = lsRegistro.get(0);
				Map map = new HashMap();
				map.put("idRegistro", layout.getIdRegistroLayoutEdi());
				map.put("nrIdentificador", layout.getIdentificador());
				list.add(map);
			}
		}		
		return list;		
	}	

	public RegistroLayoutEDIService getRegistroLayoutEDIService() {
		return registroLayoutEDIService;
	}

	public void setRegistroLayoutEDIService(
			RegistroLayoutEDIService registroLayoutEDIService) {
		this.registroLayoutEDIService = registroLayoutEDIService;
	}	
}
