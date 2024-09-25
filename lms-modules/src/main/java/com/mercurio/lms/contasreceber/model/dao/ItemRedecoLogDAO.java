package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.ItemRedecoLog;

/**
 * @spring.bean 
 */
public class ItemRedecoLogDAO extends BaseCrudDao<ItemRedecoLog, Long> {

	protected final Class getPersistentClass() {

		return ItemRedecoLog.class;
	}	
}
