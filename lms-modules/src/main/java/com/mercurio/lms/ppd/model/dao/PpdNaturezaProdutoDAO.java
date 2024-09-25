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
import com.mercurio.lms.ppd.model.PpdNaturezaProduto;

public class PpdNaturezaProdutoDAO extends BaseCrudDao<PpdNaturezaProduto, Long>  {
		
	public PpdNaturezaProduto findById(Long id) {	
		return (PpdNaturezaProduto)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdNaturezaProduto> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as natureza ")					
			.append("where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "dsNaturezaProduto") != null) {
			query.append("  and natureza.dsNaturezaProduto like :dsNaturezaProduto ");
		}
		
		query.append("order by natureza.dsNaturezaProduto");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookup(String dsNaturezaProduto) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("s.idNaturezaProduto"), "idNaturezaProduto")
			.add(Projections.property("s.dsNaturezaProduto"), "dsNaturezaProduto");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s");
		dc.setProjection(pl);

		/* Filtro */
		dc.add(Restrictions.ilike("s.dsNaturezaProduto", dsNaturezaProduto));
		
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return (List<Map<String, Object>>)getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	@Override
	protected Class getPersistentClass() {		
		return PpdNaturezaProduto.class;
	}
	
	public void store(PpdNaturezaProduto naturezaProduto) {
		super.store(naturezaProduto);
	}
}	
