package com.mercurio.lms.municipios.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.ZonaServico;
import com.mercurio.lms.municipios.model.service.ZonaServicoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterZonasServicosAction"
 */

public class ManterZonasServicosAction extends CrudAction {
	public void setZonaServico(ZonaServicoService zonaServicoService) {
		this.defaultService = zonaServicoService;
	}
    public void removeById(java.lang.Long id) {
        ((ZonaServicoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((ZonaServicoService)defaultService).removeByIds(ids);
    }

    public ZonaServico findById(java.lang.Long id) {
    	return ((ZonaServicoService)defaultService).findById(id);
    }

}
