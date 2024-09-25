package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.ParcelaRecalculoSorter;
import com.mercurio.lms.vendas.model.dao.ParcelaRecalculoSorterDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * @spring.bean id="lms.vendas.fatorCubagemDivisaoService"
 */
public class ParcelaRecalculoSorterService extends CrudService<ParcelaRecalculoSorter, Long> {
    
    public ParcelaRecalculoSorterDAO getParcelaRecalculoSorterDAO() {
        return (ParcelaRecalculoSorterDAO) getDao();
    }
    
    public void setParcelaRecalculoSorterDAO(ParcelaRecalculoSorterDAO parcelaRecalculoSorterDAO) {
        setDao(parcelaRecalculoSorterDAO);
    }

	@Override
	public Serializable store(ParcelaRecalculoSorter bean) {
		return super.store(bean);
	}
	
}
