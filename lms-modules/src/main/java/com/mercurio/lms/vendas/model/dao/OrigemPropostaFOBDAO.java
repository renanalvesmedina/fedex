package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.OrigemPropostaFOB;

public class OrigemPropostaFOBDAO extends BaseCrudDao<OrigemPropostaFOB, Long> {

	@Override
	protected Class getPersistentClass() {
		return OrigemPropostaFOB.class;
	}

	/**
	 * Obtem todas as origens relacionada a proposta fob
	 * 
	 * @param  idProposta
	 * @return List<OrigemPropostaFOB>
	 */
	public List<OrigemPropostaFOB> findFiliaisProposta(Long idProposta) {
		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("filial", "f")
		.add(Restrictions.eq("propostaFOB.id", idProposta));
		
		return findByDetachedCriteria(dc);
	}
	
}
