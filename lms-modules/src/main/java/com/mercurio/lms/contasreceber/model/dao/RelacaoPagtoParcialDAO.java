package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.RelacaoPagtoParcial;

public class RelacaoPagtoParcialDAO extends BaseCrudDao<RelacaoPagtoParcial, Long> {

	@Override
	protected Class<RelacaoPagtoParcial> getPersistentClass() {
		return RelacaoPagtoParcial.class;
	}

	public boolean validateRelacaoPagamentoParcial(Long idFatura) {
		Criteria c = getSession().createCriteria(getPersistentClass());
		c.add(Restrictions.eq("fatura.id",idFatura));
		return !c.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<RelacaoPagtoParcial> findByIdFatura(Long idFatura) {
		Criteria c = getSession().createCriteria(getPersistentClass());
		c.add(Restrictions.eq("fatura.id",idFatura));
		return c.list();
	}
	
	public BigDecimal findByIdFaturaTotalvlPagamento(Long idFatura) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT sum(r.vlPagamento) ");
		query.append(" from RelacaoPagtoParcial as r ");
		query.append(" WHERE r.fatura.idFatura = :idFatura ");
		
		Map<String, Object> criteria = new HashMap<String, Object>(1);
		criteria.put("idFatura", idFatura);
		
		return (BigDecimal)getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}

	/**
	 * LMS-6106 - Busca <tt>RelacaoPagtoParcial</tt> para a fatura relacionada a
	 * um <tt>Boleto</tt> especificado pelo id.
	 * 
	 * @param idBoleto
	 * @return lista de pagamentos parciais
	 */
	@SuppressWarnings("unchecked")
	public List<RelacaoPagtoParcial> findByIdboleto(Long idBoleto) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT rpp ")
				.append("FROM RelacaoPagtoParcial rpp ")
				.append("WHERE rpp.fatura.boleto.idBoleto = ? ");
		return getAdsmHibernateTemplate().find(hql.toString(), idBoleto);
	}

}
