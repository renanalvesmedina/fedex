package com.mercurio.lms.ppd.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdLoteRecibo;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.service.PpdLoteReciboService;
import com.mercurio.lms.ppd.model.service.PpdReciboService;
 
public class ManterLotesRecibosAction {
	private PpdReciboService reciboService;		
	private PpdLoteReciboService loteReciboService;
   
    public Map<String, Object> store(Map<String, Object> bean) {    	    	
    	PpdRecibo recibo = reciboService.findById((Long)bean.get("idRecibo"));
    	PpdLoteRecibo loteRecibo = new PpdLoteRecibo();  
    	loteRecibo.setRecibo(recibo);
    	loteReciboService.store(loteRecibo);
    	
    	Map<String,Object> retorno = new HashMap<String, Object>();
    	retorno.put("idLoteRecibo", loteRecibo.getIdLoteRecibo());
    	return retorno;
    }
   
    public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {    	
    	ResultSetPage rsp = loteReciboService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdLoteRecibo> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(int i=0;i<list.size();i++) {
			PpdLoteRecibo item = list.get(i);
			retorno.add(item.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
    }
    
    public void removeById(Long id) {
    	loteReciboService.removeById(id);
    }
    
    @ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
    	loteReciboService.removeByIds(ids);
	}

    //Sets das Services
	public void setReciboService(PpdReciboService reciboService) {
		this.reciboService = reciboService;
	}

	public void setLoteReciboService(PpdLoteReciboService loteReciboService) {
		this.loteReciboService = loteReciboService;
	}  
}