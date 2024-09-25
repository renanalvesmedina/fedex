package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdFormaPgto;
import com.mercurio.lms.ppd.model.dao.PpdFormaPgtoDAO;

public class PpdFormaPgtoService extends CrudService<PpdFormaPgto, Long> {		
	
	public PpdFormaPgto findById(Long id) {
		return getDAO().findById(id);  
	}
	
	public Serializable store(PpdFormaPgto bean) {
		return super.store(bean);		
	}
	
	public void setDAO(PpdFormaPgtoDAO dao) {
		setDao(dao);
	}
	
	public ResultSetPage<PpdFormaPgto> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}
	
	private PpdFormaPgtoDAO getDAO() {
		return (PpdFormaPgtoDAO) getDao();		
	}	
}