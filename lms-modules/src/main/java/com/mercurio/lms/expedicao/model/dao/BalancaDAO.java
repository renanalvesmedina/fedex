package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.Balanca;

public class BalancaDAO extends BaseCrudDao<Balanca, Long> {

	@Override
	protected Class getPersistentClass() {
		return Balanca.class;
	}
	
	

}
