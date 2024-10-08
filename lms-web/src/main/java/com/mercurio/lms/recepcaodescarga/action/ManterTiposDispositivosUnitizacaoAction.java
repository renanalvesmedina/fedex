package com.mercurio.lms.recepcaodescarga.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.TipoDispositivoUnitizacaoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.recepcaodescarga.manterTiposDispositivosUnitizacaoAction"
 */

public class ManterTiposDispositivosUnitizacaoAction extends CrudAction {
	public void setTipoDispositivoUnitizacao(TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
		this.defaultService = tipoDispositivoUnitizacaoService;
	}
    public void removeById(java.lang.Long id) {
        ((TipoDispositivoUnitizacaoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((TipoDispositivoUnitizacaoService)defaultService).removeByIds(ids);
    }

    public TipoDispositivoUnitizacao findById(java.lang.Long id) {
    	return ((TipoDispositivoUnitizacaoService)defaultService).findById(id);
    }

}
