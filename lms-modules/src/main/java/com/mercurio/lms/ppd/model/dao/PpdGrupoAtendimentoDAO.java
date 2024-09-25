package com.mercurio.lms.ppd.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.ppd.model.PpdGrupoAtendimento;

public class PpdGrupoAtendimentoDAO extends BaseCrudDao<PpdGrupoAtendimento, Long>  {
		
	public PpdGrupoAtendimento findById(Long id) {	
		return (PpdGrupoAtendimento)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdGrupoAtendimento> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as grupoAtendimento ")					
			.append("where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "dsGrupoAtendimento") != null) {
			query.append("  and grupoAtendimento.dsGrupoAtendimento like :dsGrupoAtendimento ");
		}
		query.append("order by grupoAtendimento.dsGrupoAtendimento");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookup(String dsGrupoAtendimento) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("s.idGrupoAtendimento"), "idGrupoAtendimento")
			.add(Projections.property("s.dsCategoria"), "dsGrupoAtendimento");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s");
		dc.setProjection(pl);

		/* Filtro */
		dc.add(Restrictions.ilike("s.dsGrupoAtendimento", dsGrupoAtendimento));
		
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return (List<Map<String, Object>>)getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
		
	protected Class getPersistentClass() {		
		return PpdGrupoAtendimento.class;
	}
	
	public void store(PpdGrupoAtendimento grupoAtendimento) {
		super.store(grupoAtendimento);
	}
}	
