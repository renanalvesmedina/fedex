package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.ItemRelacaoPagtoParcial;
import com.mercurio.lms.contasreceber.model.dao.ItemRelacaoPagtoParcialDAO;

public class ItemRelacaoPagtoParcialService extends CrudService<ItemRelacaoPagtoParcial, Long> {
	
	public void setItemRelacaoPagtoParcialDAO(ItemRelacaoPagtoParcialDAO dao) {
		setDao(dao);
	}

	private ItemRelacaoPagtoParcialDAO getItemRelacaoPagtoParcialDAO() {
		return (ItemRelacaoPagtoParcialDAO) getDao();
	}

	public List<ItemRelacaoPagtoParcial> findByIdDevedorDocServFat(Long idDevedorDocServFat) {
		return getItemRelacaoPagtoParcialDAO().findByIdDevedorDocServFat(idDevedorDocServFat);
	}

}
