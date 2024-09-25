package com.mercurio.lms.municipios.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.municipios.model.TipoPagamentoPosto;
import com.mercurio.lms.municipios.model.service.TipoPagamentoPostoService;
import com.mercurio.lms.util.JTVigenciaUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterTipoPagamentoPostoAction"
 */

public class ManterTipoPagamentoPostoAction extends CrudAction {
	public void setTipoPagamentoPosto(TipoPagamentoPostoService tipoPagamentoPostoService) {
		this.defaultService = tipoPagamentoPostoService;
	}
    public void removeById(java.lang.Long id) {
        ((TipoPagamentoPostoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((TipoPagamentoPostoService)defaultService).removeByIds(ids);
    }

    public Map findById(java.lang.Long id) {
    	TipoPagamentoPosto bean = ((TipoPagamentoPostoService)defaultService).findById(id);
    	TypedFlatMap result = new TypedFlatMap();
    	TipoPagamPostoPassagem tipoPagamPostoPassagem = bean.getTipoPagamPostoPassagem();
    	
		result.put("tipoPagamPostoPassagem.idTipoPagamPostoPassagem",tipoPagamPostoPassagem.getIdTipoPagamPostoPassagem());
    	result.put("nrPrioridadeUso",bean.getNrPrioridadeUso());
    	result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
    	result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
    	result.put("idTipoPagamentoPosto",bean.getIdTipoPagamentoPosto());
    	result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
    	return result;
    }

}
