package com.mercurio.lms.municipios.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.AtendimentoFilial;
import com.mercurio.lms.municipios.model.service.AtendimentoFilialService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterHorariosAtendimentoFilialAction"
 */

public class ManterHorariosAtendimentoFilialAction extends CrudAction {
	public void setAtendimentoFilial(AtendimentoFilialService atendimentoFilialService) {
		this.defaultService = atendimentoFilialService;
	}
    public void removeById(java.lang.Long id) {
        ((AtendimentoFilialService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((AtendimentoFilialService)defaultService).removeByIds(ids);
    }

    public AtendimentoFilial findById(java.lang.Long id) {
    	return ((AtendimentoFilialService)defaultService).findById(id);
    }

}
