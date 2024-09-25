package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.FilialEmbarcadoraLog;
import com.mercurio.lms.vendas.model.dao.FilialEmbarcadoraLogDAO;

/**
 * @spring.bean id="lms.vendas.filialEmbarcadoraLogService"
 */
public class FilialEmbarcadoraLogService extends CrudService<FilialEmbarcadoraLog, Long> {

	public void setFilialEmbarcadoraLogDAO(FilialEmbarcadoraLogDAO dao){

		setDao( dao );
	}

	private FilialEmbarcadoraLogDAO getFilialEmbarcadoraLogDAO() {

		return (FilialEmbarcadoraLogDAO) getDao();
	}
}