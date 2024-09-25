package com.mercurio.lms.municipios.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Rodovia;
import com.mercurio.lms.municipios.model.service.RodoviaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterRodoviasAction"
 */

public class ManterRodoviasAction extends CrudAction {
	public void setRodovia(RodoviaService rodoviaService) {
		this.defaultService = rodoviaService;
	}
    public void removeById(java.lang.Long id) {
        ((RodoviaService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((RodoviaService)defaultService).removeByIds(ids);
    }

    public Rodovia findById(java.lang.Long id) {
    	return ((RodoviaService)defaultService).findById(id);
    }
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return ((RodoviaService)defaultService).findPaginatedCustom(criteria);
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return ((RodoviaService)defaultService).getRowCountCustom(criteria);
	}

}
