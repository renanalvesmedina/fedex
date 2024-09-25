package com.mercurio.lms.integracao.action;


import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.integracao.model.MunicipioCorporativo;
import com.mercurio.lms.integracao.model.service.MunicipioCorporativoService;
import com.mercurio.lms.integracao.model.service.PaisCorporativoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * 
 * @spring.bean id="lms.integracao.manterMunicipiosCorporativoAction"
 */
public class ManterMunicipiosCorporativoAction extends CrudAction{
	private PaisCorporativoService paisCorporativoService;	
	
	public void removeById(java.lang.Long id) {
        getService().removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getService().removeByIds(ids);
    }

    public MunicipioCorporativo findById(java.lang.Long id) {
    	return getService().findById(id);
    }
    
    public List findLookupPais(TypedFlatMap criteria){
    	return getPaisCorporativoService().findLookup(criteria);
    }
    
	/**
	 * Consulta da grid principal
	 */
	public ResultSetPage findPaginated (TypedFlatMap criteria){
		ResultSetPage rsPage = this.getService().findPaginated(criteria);
		return rsPage;
	}
	
	/**
	 * RowCount da grid principal
	 */
	public Integer getRowCount(TypedFlatMap criteria) {
		return this.getService().getRowCount(criteria);
	}
    
	///// GETTERS E SETTERS /////
	public void setService(MunicipioCorporativoService municipioCorporativoService) {
		this.defaultService = municipioCorporativoService;
	}
	
	private MunicipioCorporativoService getService() {
		return ((MunicipioCorporativoService)defaultService);
	}

	public PaisCorporativoService getPaisCorporativoService() {
		return paisCorporativoService;
	}

	public void setPaisCorporativoService(
			PaisCorporativoService paisCorporativoService) {
		this.paisCorporativoService = paisCorporativoService;
	}

}
