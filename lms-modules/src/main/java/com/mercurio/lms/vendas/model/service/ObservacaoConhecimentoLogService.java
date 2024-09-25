package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.ObservacaoConhecimentoLog;
import com.mercurio.lms.vendas.model.dao.ObservacaoConhecimentoLogDAO;

/**
 * @spring.bean id="lms.vendas.observacaoConhecimentoLogService"
 */
public class ObservacaoConhecimentoLogService extends CrudService<ObservacaoConhecimentoLog, Long> {

	public void setObservacaoConhecimentoLogDAO(ObservacaoConhecimentoLogDAO dao){

		setDao( dao );
	}

	private ObservacaoConhecimentoLogDAO getObservacaoConhecimentoLogDAO() {

		return (ObservacaoConhecimentoLogDAO) getDao();
	}
}