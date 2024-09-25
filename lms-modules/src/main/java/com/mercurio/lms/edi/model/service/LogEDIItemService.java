package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;

import com.mercurio.lms.edi.model.LogEDIItem;

import com.mercurio.lms.edi.model.dao.LogEDIItemDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.logEDIItemService"
 */

public class LogEDIItemService extends CrudService<LogEDIItem, Long> {

	

	@Override
	public LogEDIItem findById(Long id) {		
		return (LogEDIItem)super.findById(id);
	}
	
	public ResultSetPage findPaginatedByIdLogDetalhe(TypedFlatMap criteria) {	
		ResultSetPage rsp = getLogEDIItemDAO().findPaginatedByIdLogDetalhe(criteria, FindDefinition.createFindDefinition(criteria));
		return rsp;
	}
	public Integer getRowCountByIdLogDetalhe(TypedFlatMap criteria) {	
		return getLogEDIItemDAO().getRowCountByIdLogDetalhe(criteria);
	}
	
	@Override
	public Serializable store(LogEDIItem bean) {
		if(bean.getIdItem() == null){
			bean.setIdItem(getLogEDIItemDAO().findSequence());
		}
		return super.store(bean);
	}
	
	private LogEDIItemDAO getLogEDIItemDAO() {
        return (LogEDIItemDAO) getDao();
    }
    
    public void setLogEDIItemDAO(LogEDIItemDAO dao) {
        setDao(dao);
    }	
}
