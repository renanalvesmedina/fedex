package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.TipoVisita;
import com.mercurio.lms.vendas.model.service.TipoVisitaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterTiposVisitaAction"
 */

public class ManterTiposVisitaAction extends CrudAction {
	
	public void setTipoVisita(TipoVisitaService tipoVisitaService) {
		this.defaultService = tipoVisitaService;
	}
    public void removeById(java.lang.Long id) {
    	((TipoVisitaService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
   	((TipoVisitaService)defaultService).removeByIds(ids);
   }

    public TipoVisita findById(java.lang.Long id) {
    	return ((TipoVisitaService)defaultService).findById(id);
    }
    
	public Serializable store(TipoVisita bean) {
		return ((TipoVisitaService)defaultService).store(bean);
	}
	
	private TipoVisitaService getService() {
		return ((TipoVisitaService)defaultService);
	}
	
	
	public ResultSetPage findPaginated(Map criteria) {    
		 addEmpresaOnMap(criteria);
		 return getService().findPaginated(criteria);
	}
	    
	
	public Integer getRowCount(Map criteria) {    
		addEmpresaOnMap(criteria);
	   	return getService().getRowCount(criteria);
	}
	    	
	private Long getIdEmpresaUsuarioLogado()
	{
	   	return SessionUtils.getEmpresaSessao().getIdEmpresa();
	}
	
	private void addEmpresaOnMap(Map dados)
	{
		dados.put("ID_EMPRESA_LOG_USER",getIdEmpresaUsuarioLogado());
	}

}
