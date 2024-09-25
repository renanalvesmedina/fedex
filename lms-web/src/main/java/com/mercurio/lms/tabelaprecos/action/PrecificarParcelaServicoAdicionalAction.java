package com.mercurio.lms.tabelaprecos.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.ValorServicoAdicionalService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.precificarParcelaServicoAdicionalAction"
 */

public class PrecificarParcelaServicoAdicionalAction extends CrudAction {
	
	private TabelaPrecoService tabelaPrecoService;
	
	public void setValorServicoAdicional(ValorServicoAdicionalService valorServicoAdicionalService) {
		this.defaultService = valorServicoAdicionalService;
	}
    public void removeById(java.lang.Long id) {
        ((ValorServicoAdicionalService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((ValorServicoAdicionalService)defaultService).removeByIds(ids);
    }

    public ValorServicoAdicional findById(java.lang.Long id) {
    	return ((ValorServicoAdicionalService)defaultService).findById(id);
    }
    
    public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
    
    public Serializable storeAtualizaTabela(ValorServicoAdicional bean) {
    	TypedFlatMap toReturn = new TypedFlatMap();
		
		toReturn.put("idValorServicoAdicional", this.store(bean));
		ValorServicoAdicional beanRetorno = this.findById(bean.getIdValorServicoAdicional());
		
		Long idTabelaPrecoParcela = (beanRetorno.getTabelaPrecoParcela() != null && beanRetorno.getTabelaPrecoParcela().getIdTabelaPrecoParcela() != null) ? beanRetorno.getTabelaPrecoParcela().getIdTabelaPrecoParcela() : null;
		String msgAtualizacaoAutomatica = tabelaPrecoService.findAtualizaTabelaCustos(idTabelaPrecoParcela);

		toReturn.put("msgAtualizacaoAutomatica", msgAtualizacaoAutomatica);
		return toReturn;
	}

}
