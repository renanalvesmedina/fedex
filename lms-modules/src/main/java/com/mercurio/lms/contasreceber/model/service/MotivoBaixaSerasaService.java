package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.MotivoBaixaSerasa;
import com.mercurio.lms.contasreceber.model.dao.MotivoBaixaSerasaDAO;


public class MotivoBaixaSerasaService extends CrudService<MotivoBaixaSerasa, Long> {
	

	public void setLoteSerasaDAO(MotivoBaixaSerasaDAO dao) {
		setDao(dao);
	}
	
	public MotivoBaixaSerasa findById(Long id){
		return getMotivoDao().findById(id);
	}

	public MotivoBaixaSerasa store(final MotivoBaixaSerasa loteSerasa) {
		return (MotivoBaixaSerasa)getMotivoDao().store(loteSerasa);
	}
	
	public MotivoBaixaSerasaDAO getMotivoDao(){
		return (MotivoBaixaSerasaDAO) getDao();
	}

	public List findMotivosBaixa() {
		return getMotivoDao().findMotivosBaixa();
	}
	
}
