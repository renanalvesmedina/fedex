package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.TipoArquivoEDI;
import com.mercurio.lms.edi.model.TipoLayoutDocumento;
import com.mercurio.lms.edi.model.service.LayoutEDIService;
import com.mercurio.lms.edi.model.service.TipoArquivoEDIService;
import com.mercurio.lms.edi.model.service.TipoLayoutDocumentoService;

public class ManterLayoutEDIAction {

	private LayoutEDIService layoutEDIService;
	private TipoLayoutDocumentoService tipoLayoutDocumentoService;
	private TipoArquivoEDIService tipoArquivoEDIService;
	
	/**
	 * Obtem o LayoutEDI através do ID
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = layoutEDIService.findById(id).getMapped();
    	return bean;
	}	
	
	
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = layoutEDIService.findPaginated(new PaginatedQuery(criteria));					
		
		List<LayoutEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(LayoutEDI layout : list){
			retorno.add(layout.getMapped());
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
		 
		LayoutEDI pojo = new LayoutEDI();	
		
		pojo.setIdLayoutEdi((Long)bean.get("idLayoutEdi"));
		pojo.setDsLayoutEdi((String)bean.get("dsLayoutEdi"));
		pojo.setNmLayoutEdi((String)bean.get("nmLayoutEdi"));
		pojo.setQtLayoutEdi((Integer)bean.get("qtLayoutEdi"));		 		
		pojo.setTpLayoutEdi(new DomainValue((String)bean.get("tpLayoutEdi")));
		 
		/*Tipo Arquivo Edi*/	
		TipoArquivoEDI tipoArquivoEDI  = new TipoArquivoEDI();
		tipoArquivoEDI.setIdTipoArquivoEDI((Long)bean.get("idTipoArquivoEDI"));
		pojo.setTipoArquivoEDI(tipoArquivoEDI);
		 
		/*Tipo layout Documento*/
		TipoLayoutDocumento tipoLayoutDocumento = new TipoLayoutDocumento();
		tipoLayoutDocumento.setIdTipoLayoutDocumento((Long) bean.get("idTipoLayoutDocumento"));		 
		pojo.setTipoLayoutDocumento(tipoLayoutDocumento);
		 
		/*Salva o Layout EDI*/
		layoutEDIService.store(pojo);
			 
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idLayoutEdi", pojo.getIdLayoutEdi());
			 
		return retorno;
	}	
	
	/**
	 * Remove uma lista de LayoutEDI
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		layoutEDIService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * @param id
	 */
	public void removeById(Long id) {
		layoutEDIService.removeById(id);
	}	
	
	
	/**
	 * Obtem a lista de tipos de documentos através da tabela TIPO_LAYOUT_DOCUMENTO
	 * 
	 * @return List
	 */
	public List findListTpLayoutDocumento(){	
		return tipoLayoutDocumentoService.findListTpLayoutDocumento();
	}
	
	/**
	 * Obtem a lista de extensoes através da tabela TIPO_ARQUIVO_EDI
	 * 
	 * @return List
	 */
	public List findListExtesaoArquivo(){	
		return tipoArquivoEDIService.findListExtesaoArquivo();
	}
	
	/**
	 * Lookup LayoutEDI 
	 * 
	 * @return List
	 */
	public List findLookup(Map criteria){
		List lista = layoutEDIService.findLookup(criteria);		
		if (!lista.isEmpty() && lista.size() == 1) {
			LayoutEDI layout = (LayoutEDI)lista.get(0);
			Map map = new HashMap();
			map.put("idLayoutEdi", layout.getIdLayoutEdi());
			map.put("nmLayoutEdi", layout.getNmLayoutEdi());
			map.put("tpLayoutEdi", layout.getTpLayoutEdi().getValue());
			map.put("extTipoArquivoEDI", layout.getTipoArquivoEDI().getExtTipoArquivoEDI().toString());
			
			lista.add(map);
			lista.remove(layout);
		}
		return lista;		
	}


	public LayoutEDIService getLayoutEDIService() {
		return layoutEDIService;
	}


	public void setLayoutEDIService(LayoutEDIService layoutEDIService) {
		this.layoutEDIService = layoutEDIService;
	}


	public TipoLayoutDocumentoService getTipoLayoutDocumentoService() {
		return tipoLayoutDocumentoService;
	}


	public void setTipoLayoutDocumentoService(
			TipoLayoutDocumentoService tipoLayoutDocumentoService) {
		this.tipoLayoutDocumentoService = tipoLayoutDocumentoService;
	}


	public TipoArquivoEDIService getTipoArquivoEDIService() {
		return tipoArquivoEDIService;
	}


	public void setTipoArquivoEDIService(TipoArquivoEDIService tipoArquivoEDIService) {
		this.tipoArquivoEDIService = tipoArquivoEDIService;
	}
	
}
