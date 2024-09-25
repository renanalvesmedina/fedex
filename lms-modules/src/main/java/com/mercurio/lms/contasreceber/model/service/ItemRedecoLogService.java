package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.dao.ItemRedecoLogDAO;
import com.mercurio.lms.contasreceber.model.ItemRedecoLog;

/**
 * @spring.bean id="lms.contasreceber.itemRedecoLogService"
 */
public class ItemRedecoLogService extends CrudService<ItemRedecoLog, Long> {

	public final void setItemRedecoLogDAO(ItemRedecoLogDAO dao){

		setDao(dao);
	}

	public final ItemRedecoLogDAO getItemRedecoLogDAO() {

		return (ItemRedecoLogDAO) getDao();
	}
}