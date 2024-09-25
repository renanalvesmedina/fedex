package com.mercurio.lms.franqueados.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.AnexoLancamentoFranqueado;

public class AnexoLancamentoFranqueadoDAO  extends BaseCrudDao<AnexoLancamentoFranqueado, Long> {

	@Override
	protected Class<AnexoLancamentoFranqueado> getPersistentClass() {
		return AnexoLancamentoFranqueado.class;
	}
	
	@Override
	public AnexoLancamentoFranqueado findById(Long id) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as alf ");
		query.append("inner join fetch alf.lancamento lf ");
		query.append("left join fetch lf.pendencia pe ");
		query.append("inner join fetch lf.franquia fr ");
		query.append("inner join fetch fr.filial fil ");
		query.append("inner join fetch fil.pessoa p ");
		query.append("where ");
		query.append(" alf.idAnexoLancamentoFrq = :id ");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		
		return (AnexoLancamentoFranqueado) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}

	public List findByLancamentoFrq(Long idLancamento) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as alf ");
		query.append("inner join fetch alf.lancamento lf ");
		query.append("left join fetch lf.pendencia pe ");
		query.append("inner join fetch lf.franquia fr ");
		query.append("inner join fetch fr.filial fil ");
		query.append("inner join fetch fil.pessoa p ");
		query.append("where ");
		query.append(" lf.idLancamentoFrq = ? ");
		
		return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idLancamento});
	}
}
