/*
 * Created on Sep 26, 2005
 *
 */
package com.mercurio.lms.configuracoes.model.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.dao.MediaIndicadoreFinanceiroMoedaDAO;

/**
 * @author Mickaël Jalbert
 * @spring.bean id="lms.configuracoes.mediaIndicadoreFinanceiroMoedaService"
 */
public class MediaIndicadoreFinanceiroMoedaService {

	private DomainValueService domainValueService;
	private MediaIndicadoreFinanceiroMoedaDAO mediaIndicadoreFinanceiroMoedaDAO;

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#findPaginated(java.util.Map)
	 */
	public ResultSetPage findMensalPaginated(Map criteria){
		ResultSetPage rsp = null;
		Map<String, List> listsMap = getMediaIndicadoreFinanceiroMoedaDAO().findMensal(criteria);
		if (listsMap != null) {
			rsp = new ResultSetPage(Integer.valueOf(1), incrementarPercentualDominio(listsMap.get("list1"), listsMap.get("list2")));
		}
		return rsp;
	}
	
	private List incrementarPercentualDominio(List list, List list2){
		Double mediaAnterior = null;
		Double percentual = new Double(0d);

		if (list2 != null && !list2.isEmpty()) {
			Map row2 = (Map)list2.get(0);
			mediaAnterior = (Double)row2.get("media");
		}
		
		for(Iterator iter = list.iterator(); iter.hasNext();){
			//buscar a media do mes de dezembro do ano anterior
			Map row = (Map)iter.next();

			if (mediaAnterior != null) {
				percentual = (((Double)row.get("media") / mediaAnterior.doubleValue()) * 100) - 100;
			} else {
				percentual = 0d;
			}
			row.put("percentual",percentual);
			row.put("mesDominio",this.domainValueService.findDomainValueByValue("DM_MES", ((Integer)row.get("mes")).toString()).getDescription().getValue());			
			mediaAnterior = (Double)row.get("media");
		}
		return list;
	}

	public ResultSetPage findDiarioPaginated(Map criteria){
		return getMediaIndicadoreFinanceiroMoedaDAO().findDiario(criteria);
	}	
	
	public Map findById(java.lang.Long  id){
		Map map = new HashMap();
		map.put("mes",id);
		return map;
	}	
	
	public void setMediaIndicadoreFinanceiroMoedaDAO(MediaIndicadoreFinanceiroMoedaDAO dao){
		this.mediaIndicadoreFinanceiroMoedaDAO = dao;
	}
	
	public MediaIndicadoreFinanceiroMoedaDAO getMediaIndicadoreFinanceiroMoedaDAO(){
		return this.mediaIndicadoreFinanceiroMoedaDAO;
	}
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
}

