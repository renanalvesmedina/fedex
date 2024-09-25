package com.mercurio.lms.tabelaprecos.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoRotaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.manterTarifasPrecoRotasAction"
 */

public class ManterTarifasPrecoRotasAction extends CrudAction {
	public void setTarifaPrecoRota(TarifaPrecoRotaService tarifaPrecoRotaService) {
		this.defaultService = tarifaPrecoRotaService;
	}
    public void removeById(java.lang.Long id) {
        ((TarifaPrecoRotaService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((TarifaPrecoRotaService)defaultService).removeByIds(ids);
    }

    public TarifaPrecoRota findById(java.lang.Long id) {
    	return ((TarifaPrecoRotaService)defaultService).findById(id);
    }

}
