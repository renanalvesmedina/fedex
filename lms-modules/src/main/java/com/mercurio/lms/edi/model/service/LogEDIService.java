package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.dao.LogEDIDAO;



public class LogEDIService extends CrudService<LogEDI, Long> {

	
	@Override
	public LogEDI findById(Long id) {		
		return (LogEDI)super.findById(id);
	}
	
	
	
	@Override
	public Serializable store(LogEDI bean) {
		if(bean.getIdLogEdi() == null){
			bean.setIdLogEdi(getLogEDIDAO().findSequence());
		}
		return super.store(bean);
	}
	
	public ResultSetPage findPaginatedLog(TypedFlatMap criteria) {	
		ResultSetPage rsp = getLogEDIDAO().findPaginatedLog(criteria, FindDefinition.createFindDefinition(criteria));
		return rsp;
	}
	public Integer getRowCountLog(TypedFlatMap criteria) {	
		return getLogEDIDAO().getRowCountLog(criteria);
	}

	
	private LogEDIDAO getLogEDIDAO() {
        return (LogEDIDAO) getDao();
    }
    
    public void setLogEDIDAO(LogEDIDAO dao) {
        setDao(dao);
    }	
	
}
