package com.mercurio.lms.ppd.swt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;
import com.mercurio.lms.ppd.model.service.PpdStatusReciboService;

public class ManterStatusRecibosAction {
	PpdStatusReciboService statusReciboService;

	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = statusReciboService.findById(id).getMapped();
    	return bean;
	}
	
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = statusReciboService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdStatusRecibo> listStatus = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(listStatus.size());
		
		for(int i=0;i<listStatus.size();i++) {
			PpdStatusRecibo status = listStatus.get(i);
			retorno.add(status.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
	}

	public void setStatusReciboService(PpdStatusReciboService statusReciboService) {
		this.statusReciboService = statusReciboService;
	}	
}
