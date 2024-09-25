package com.mercurio.lms.questionamentoFaturas.model.dao;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.questionamentofaturas.model.AnexoQuestionamentoFatura;

public class AnexoQuestionamentoFaturasDAO extends BaseCrudDao<AnexoQuestionamentoFatura, Long> {

	@Override
	protected Class getPersistentClass() {
		return AnexoQuestionamentoFatura.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("questionamentoFatura", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);
	}

	public AnexoQuestionamentoFatura findById(Long idAnexoQuestionamentoFatura) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("id", idAnexoQuestionamentoFatura));
		return (AnexoQuestionamentoFatura)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ResultSetPage<AnexoQuestionamentoFatura> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from AnexoQuestionamentoFatura as a ")
			.append("	inner join fetch a.questionamentoFatura as q ")
			.append("	inner join fetch a.usuario as u ")
			.append("where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idAnexoQuestionamentoFatura") != null) {
			query.append("  and a.id = :idAnexoQuestionamentoFatura ");
		}
		if(MapUtils.getObject(criteria, "idQuestionamentoFatura") != null) {
			query.append("  and q.id = :idQuestionamentoFatura ");
		}
		query.append("order by a.dhCriacao.value desc ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	public Serializable store(AnexoQuestionamentoFatura anexoQuestionamentoFatura) {
		super.store(anexoQuestionamentoFatura);
		return anexoQuestionamentoFatura.getIdAnexoQuestionamentoFatura();
	}
}