package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.InformacaoDocServicoLogDAO;
import com.mercurio.lms.expedicao.model.InformacaoDocServicoLog;

/**
 * @spring.bean id="lms.expedicao.informacaoDocServicoLogService"
 */
public class InformacaoDocServicoLogService extends CrudService<InformacaoDocServicoLog, Long> {

	public final void setInformacaoDocServicoLogDAO(InformacaoDocServicoLogDAO dao){

		setDao(dao);
	}

	public final InformacaoDocServicoLogDAO getInformacaoDocServicoLogDAO() {

		return (InformacaoDocServicoLogDAO) getDao();
	}
}