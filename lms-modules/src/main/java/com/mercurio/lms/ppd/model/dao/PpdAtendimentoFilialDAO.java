package com.mercurio.lms.ppd.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.ppd.model.PpdAtendimentoFilial;

public class PpdAtendimentoFilialDAO extends BaseCrudDao<PpdAtendimentoFilial, Long>  {
		
	public PpdAtendimentoFilial findById(Long id) {	
		return (PpdAtendimentoFilial)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdAtendimentoFilial> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as atendimentoFilial ")
			.append("	inner join fetch atendimentoFilial.grupoAtendimento as grupoAtendimento ")
			.append("	inner join fetch atendimentoFilial.filial as filial ")
			.append("	inner join fetch filial.pessoa as pessoa ")
			.append("where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idGrupoAtendimento") != null) {
			query.append("  and grupoAtendimento.idGrupoAtendimento = :idGrupoAtendimento ");
		}
		
		query.append("order by pessoa.nmFantasia");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	public List<PpdAtendimentoFilial> findByIdFilial(Long idFilial) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("atendimentoFilial");
		
		hql.addLeftOuterJoin(getPersistentClass().getName(),"atendimentoFilial");
		hql.addCriteria("atendimentoFilial.filial.idFilial","=",idFilial);
				
		return (List)getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	protected Class getPersistentClass() {		
		return PpdAtendimentoFilial.class;
	}
	
	public void store(PpdAtendimentoFilial atendimentoFilial) {
		super.store(atendimentoFilial);
	}
}	
