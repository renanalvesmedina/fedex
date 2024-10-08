package com.mercurio.lms.configuracoes.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.configuracoes.model.service.RamoAtividadeService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.manterRamosAtividadeAction"
 */

public class ManterRamosAtividadeAction extends CrudAction {
	public void setRamoAtividade(RamoAtividadeService ramoAtividadeService) {
		this.defaultService = ramoAtividadeService;
	}
    public void removeById(java.lang.Long id) {
        ((RamoAtividadeService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((RamoAtividadeService)defaultService).removeByIds(ids);
    }

    public RamoAtividade findById(java.lang.Long id) {
    	return ((RamoAtividadeService)defaultService).findById(id);
    }

}
