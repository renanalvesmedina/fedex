package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ReciboAnuarioRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.ReciboAnuarioRfcDAO;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;

/**
 * Service responsável por operações de descontos relativos ao recibo frete
 * carreteiro. <br>
 * Compreende também operações de manipulação nas tabelas de desconto
 * relacionadas, tais como parcelas do desconto e tipo de desconto.
 * 
 */
public class ReciboAnuarioRfcService extends CrudService<ReciboAnuarioRfc, Long> {

	
	
	private ReciboAnuarioRfcDAO getReciboAnuarioRfcDAO() {
        return (ReciboAnuarioRfcDAO) getDao();
    }
	
	public void setDescontoRfcDAO(ReciboAnuarioRfcDAO dao) {
        setDao(dao);
    }
	
	
	@Override
	public Serializable store(ReciboAnuarioRfc bean) {
		return super.store(bean);
	}
	
	public DescontoRfc findById(java.lang.Long id) {
		return (DescontoRfc) super.findById(id);
	}
	
	/**
	 * Não é possível excluir um desconto.
	 */
	public void removeById(java.lang.Long id) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Não é possível excluir um desconto.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@SuppressWarnings({ "rawtypes" })
	public void removeByIds(List ids) {
		throw new UnsupportedOperationException();
	}

	public boolean hasAnuarioVinculado(ReciboFreteCarreteiro recibofreteCarreteiro) {
		return getReciboAnuarioRfcDAO().hasAnuarioVinculado(recibofreteCarreteiro);
	}
}