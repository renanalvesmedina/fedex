package com.mercurio.lms.sim.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.service.FaseProcessoService;

public class ManterFaseProcessoAction {
	
	private FaseProcessoService faseProcessoService;
	
	public Map<String, Object> store(Map<String, Object> bean) {
		FaseProcesso faseProcesso = new FaseProcesso();	
		faseProcesso.setIdFaseProcesso((Long)bean.get("idFaseProcesso"));
		faseProcesso.setCdFase((Short)bean.get("cdFase"));
		faseProcesso.setDsFase((String)bean.get("dsFase"));
		
		faseProcessoService.store(faseProcesso);
			 
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idFaseProcesso", faseProcesso.getIdFaseProcesso());
			 
		return retorno;
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		faseProcessoService.removeByIds(ids);
	}
	
	public void removeById(Long id) {
		faseProcessoService.removeById(id);
	}
	
	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = faseProcessoService.findById(id).getMapped();
    	return bean;
	}	

	public FaseProcessoService getFaseProcessoService() {
		return faseProcessoService;
	}

	public void setFaseProcessoService(FaseProcessoService faseProcessoService) {
		this.faseProcessoService = faseProcessoService;
	}

}
