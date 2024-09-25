package com.mercurio.lms.edi.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.AppleLogEdi;

public class AppleLogEdiDAO extends BaseCrudDao<AppleLogEdi, Long> {

	public AppleLogEdi findById(Long id) {	
		return (AppleLogEdi)super.findById(id);
	}
	
	@Override
	protected Class getPersistentClass() {
		return AppleLogEdi.class;
	}
}
