package com.mercurio.lms.questionamentoFaturas.model.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.questionamentofaturas.model.HistoricoQuestionamentoFatura;

public class HistoricoQuestionamentoFaturasDAO extends BaseCrudDao<HistoricoQuestionamentoFatura, Long> {

	@Override
	protected Class getPersistentClass() {
		return HistoricoQuestionamentoFatura.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("questionamentoFatura", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);
	}

	public HistoricoQuestionamentoFatura findById(Long idHistoricoQuestionamentoFatura) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("id", idHistoricoQuestionamentoFatura));
		return (HistoricoQuestionamentoFatura)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ResultSetPage<HistoricoQuestionamentoFatura> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from HistoricoQuestionamentoFatura as h ")
			.append("	inner join fetch h.questionamentoFatura as q ")
			.append("	inner join fetch h.usuario as u ")
			.append("where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idHistoricoQuestionamentoFatura") != null) {
			query.append("  and h.id = :idHistoricoQuestionamentoFatura ");
		}
		if(MapUtils.getObject(criteria, "idQuestionamentoFatura") != null) {
			query.append("  and q.id = :idQuestionamentoFatura ");
		}
		query.append("order by h.dhHistorico.value desc");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	public ResultSetPage<HistoricoQuestionamentoFatura> findPaginatedHistoricoQuestionamentoFaturas(Long idQuestionamentoFatura, FindDefinition findDefinition) {
		StringBuilder query = new StringBuilder()
			.append("select new map(h.dhHistorico as dhHistorico, h.tpHistorico as tpHistorico, h.obHistorico as obHistorico, u.usuarioADSM.nmUsuario as nmUsuario) ")
			.append("from HistoricoQuestionamentoFatura as h ")
			.append("inner join h.questionamentoFatura as q ")
			.append("inner join h.usuario as u ")
			.append("where q.id = :idQuestionamentoFatura ")
			.append("order by h.dhHistorico.value desc");
		Map parameters = new HashMap<String, Object>();
		parameters.put("idQuestionamentoFatura", idQuestionamentoFatura);
		return getAdsmHibernateTemplate().findPaginated(query.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), parameters);
	}

	public Serializable store(HistoricoQuestionamentoFatura historicoQuestionamentoFatura) {
		super.store(historicoQuestionamentoFatura);
		return historicoQuestionamentoFatura.getIdHistoricoQuestionamentoFatura();
	}
}