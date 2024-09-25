package com.mercurio.lms.rnc.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.ItemOcorrenciaNc;
import com.mercurio.lms.rnc.model.dao.ItemOcorrenciaNcDAO;

public class ItemOcorrenciaNcService extends CrudService<ItemOcorrenciaNc, Long> {

	public void setItemOcorrenciaNcDAO(ItemOcorrenciaNcDAO itemOcorrenciaNcDao) {
		setDao(itemOcorrenciaNcDao);
	}

	private ItemOcorrenciaNcDAO getItemOcorrenciaNcDAO() {
		return (ItemOcorrenciaNcDAO) getDao();
	}
	
	public Serializable store(ItemOcorrenciaNc bean) {
		return super.store(bean);
	}

}
