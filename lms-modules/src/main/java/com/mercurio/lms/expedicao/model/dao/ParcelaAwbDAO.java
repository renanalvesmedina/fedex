package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ParcelaAwb;

public class ParcelaAwbDAO extends BaseCrudDao<ParcelaAwb, Long>{

	@Override
	protected Class getPersistentClass() {
		return ParcelaAwb.class;
	}

}
