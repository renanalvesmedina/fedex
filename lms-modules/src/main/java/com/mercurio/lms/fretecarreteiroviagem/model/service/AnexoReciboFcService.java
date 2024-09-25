package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteiroviagem.model.AnexoReciboFc;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.AnexoReciboFcDAO;

public class AnexoReciboFcService extends CrudService<AnexoReciboFc, Long>{

	public AnexoReciboFc findById(java.lang.Long id) {
		return (AnexoReciboFc)super.findById(id);
	}
		
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }
	
	public Serializable store(AnexoReciboFc bean) {
        return super.store(bean);
    }
	
	private AnexoReciboFcDAO getAnexoReciboFcDAO() {
        return (AnexoReciboFcDAO) getDao();
    }
	
	public void setAnexoReciboFcDAO(AnexoReciboFcDAO dao) {
        setDao(dao);
    }
	
	@SuppressWarnings("rawtypes")
	public List findItensByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		return getAnexoReciboFcDAO().findItensByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
    }

	public Integer getRowCountItensByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		return getAnexoReciboFcDAO().getRowCountItensByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
	}	

	/**
	 * Grava uma lista de entidades AnexoReciboFc, definindo o seu relacionando
	 * com a entidade ReciboFreteCarreteiro.
	 * 
	 * @param rfc
	 * @param listAnexoReciboFc
	 */
	public void storeAnexos(ReciboFreteCarreteiro rfc, List<AnexoReciboFc> listAnexoReciboFc) {
		for (AnexoReciboFc anexoReciboFc : listAnexoReciboFc) {
			anexoReciboFc.setReciboFreteCarreteiro(rfc);
		}
		
		super.storeAll(listAnexoReciboFc);
	}
}