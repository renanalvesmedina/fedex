package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ProprietarioRPA;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.ProprietarioRpaDAO;

public class ProprietarioRpaService extends CrudService<ProprietarioRPA, Long> {
	
	
	@Override
	public Serializable store(ProprietarioRPA bean) {	
		return super.store(bean);
	}
	
	private ProprietarioRpaDAO getProprietarioRpaDAO() {
        return (ProprietarioRpaDAO) getDao();
    }
	
	public Long findProximoNumero(Long idProprietario){
		return getProprietarioRpaDAO().findProximoNumero(idProprietario);
		
	}


	public void setProprietarioRpaDAO(ProprietarioRpaDAO proprietarioRpaDAO) {
		setDao( proprietarioRpaDAO );
	}

	public boolean isGerarRPA(Long idReciboFreteCarreteiro) {		
		return getProprietarioRpaDAO().isGerarRPA(idReciboFreteCarreteiro);
	}
}