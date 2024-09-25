package com.mercurio.lms.tributos.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tributos.model.service.TipoTributacaoUfLogService;

/**
 * @spring.bean id="lms.tributos.consultarTipoTributacaoUfLogAction"
 */
public class ConsultarTipoTributacaoUfLogAction extends CrudAction {

	public void setService(TipoTributacaoUfLogService service) {

		this.defaultService = service;
	}
	
	public List findLookupUF(Map criteria) {
		
		 return ((TipoTributacaoUfLogService)this.defaultService).findLookupUF(criteria);
	 } 
}