package com.mercurio.lms.rnc.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.rnc.model.ItemOcorrenciaNc;

public class ItemOcorrenciaNcDAO  extends BaseCrudDao<ItemOcorrenciaNc, Long>{

	@Override
	protected Class getPersistentClass() {
		return ItemOcorrenciaNc.class;	
	}

}
