package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.ppd.model.PpdReciboNumeracao;
import com.mercurio.lms.ppd.model.dao.PpdReciboNumeracaoDAO;

@Assynchronous(name = "ReciboIndenizacaoPpdService")
public class PpdReciboNumeracaoService extends CrudService<PpdReciboNumeracao, Long> {

	public Serializable store(PpdReciboNumeracao bean) {
		return super.store(bean);		
	}
	
	public PpdReciboNumeracao findById(Long id) {
		return getDAO().findById(id);
	}
	
	public PpdReciboNumeracao findByIdFilial(Long idFilial) {
		return getDAO().findByIdFilial(idFilial);
	}
	
	//Sets DAO e Services
	public void setDAO(PpdReciboNumeracaoDAO dao) {
		setDao(dao);	
	}
	private PpdReciboNumeracaoDAO getDAO() {
		return (PpdReciboNumeracaoDAO) getDao();		
	}
}
