package com.mercurio.lms.municipios.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.MunicipioFilialSegmento;
import com.mercurio.lms.municipios.model.service.MunicipioFilialSegmentoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterSegmentosMercadoAtendidosAction"
 */

public class ManterSegmentosMercadoAtendidosAction extends CrudAction {
	public void setMunicipioFilialSegmento(MunicipioFilialSegmentoService municipioFilialSegmentoService) {
		this.defaultService = municipioFilialSegmentoService;
	}
    public void removeById(java.lang.Long id) {
        ((MunicipioFilialSegmentoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((MunicipioFilialSegmentoService)defaultService).removeByIds(ids);
    }

    public MunicipioFilialSegmento findById(java.lang.Long id) {
    	return ((MunicipioFilialSegmentoService)defaultService).findById(id);
    }

}
