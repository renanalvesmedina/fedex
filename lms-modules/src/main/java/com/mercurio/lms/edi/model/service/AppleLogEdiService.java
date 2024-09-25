package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.AppleLogEdi;
import com.mercurio.lms.edi.model.dao.AppleLogEdiDAO;

public class AppleLogEdiService extends CrudService<AppleLogEdi, Long> {
	
	public Serializable store(AppleLogEdi bean) {
		return super.store(bean);		
	}	
	
	public AppleLogEdi findById(Long id) {
		return getDAO().findById(id); 
	}
	
	public void setDAO(AppleLogEdiDAO dao) {
		setDao(dao);
	}
	
	private AppleLogEdiDAO getDAO() {
		return (AppleLogEdiDAO) getDao();		
	}	
	
	
}
