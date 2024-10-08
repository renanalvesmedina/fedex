package com.mercurio.lms.municipios.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.Zona;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.ZonaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.swt.manterPaisesAction"
 */

public class ManterPaisesAction extends CrudAction {
	private ZonaService zonaService;
	
	public void setPais(PaisService paisService) {
		this.defaultService = paisService;
	}
	
	public PaisService getPaisService() {
		return (PaisService)this.defaultService;
	}
	
    public void removeById(java.lang.Long id) {
        ((PaisService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((PaisService)defaultService).removeByIds(ids);
    }

    public Pais findById(java.lang.Long id) {
    	return ((PaisService)defaultService).findById(id);
    }
    
    public List findZona(Map criteria) {
    	List result = getZonaService().find(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
			Zona zona = (Zona)iter.next();
			Map include = new HashMap();
			include.put("idZona", zona.getIdZona());
			include.put("dsZona", zona.getDsZona());
			include.put("tpSituacao", zona.getTpSituacao());
			
			retorno.add(include);
		}    	
    	return retorno;
    }
    
    
    /**
     * FindPaginated de Paises.
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedPais(Map criteria) {
    	Map mapCriteria = createFindCriteria(criteria);
		ResultSetPage resultSetPage = getPaisService().findPaginated(mapCriteria);
		List listReturn = new ArrayList();
		
		for (Iterator iter = resultSetPage.getList().iterator(); iter.hasNext();) {
			Pais pais = (Pais) iter.next();
			
			Map mapReturn = new HashMap();			
			mapReturn.put("idPais", pais.getIdPais());
			mapReturn.put("nmPais", pais.getNmPais());
			mapReturn.put("sgPais", pais.getSgPais());
			mapReturn.put("cdIso", pais.getCdIso());
			if (pais.getZona() != null) {
				mapReturn.put("dsZona", pais.getZona().getDsZona());
			}
			mapReturn.put("tpSituacao", pais.getTpSituacao());
			
			listReturn.add(mapReturn);
		}
		resultSetPage.setList(listReturn);
		return resultSetPage;
    }
    
    /**
     * getRowCount de Paises.
     * @param criteria
     * @return
     */
    public Integer getRowCountPais(Map criteria) {
    	Map mapCriteria = createFindCriteria(criteria);
    	return getPaisService().getRowCount(mapCriteria);
    }
    
    
    /**
     * Cria mapa com filtros para pesquisa.
     * @param criteria
     * @return
     */
	private Map createFindCriteria(Map criteria) {
		Map mapCriteria = new HashMap();
		mapCriteria.put("_currentPage", criteria.get("_currentPage"));
    	mapCriteria.put("_pageSize", criteria.get("_pageSize"));
    	mapCriteria.put("_order", criteria.get("_order"));
    	
		mapCriteria.put("sgPais", criteria.get("sgPais"));
		mapCriteria.put("sgResumida", criteria.get("sgResumida"));
		mapCriteria.put("nmPais", criteria.get("nmPais"));
		mapCriteria.put("cdIso", criteria.get("cdIso"));
		
		Map mapZona = new HashMap();
		mapZona.put("idZona", criteria.get("idZona"));		
		mapCriteria.put("zona", mapZona);
		
		mapCriteria.put("tpBuscaEndereco", criteria.get("tpBuscaEndereco"));
		mapCriteria.put("tpSituacao", criteria.get("tpSituacao"));
		
    	return mapCriteria;
	}     
    
    
	public ZonaService getZonaService() {
		return zonaService;
	}
	public void setZonaService(ZonaService zonaService) {
		this.zonaService = zonaService;
	}
	
}
