package com.mercurio.lms.ppd.model.dao;

import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdFormaPgto;

public class PpdFormaPgtoDAO extends BaseCrudDao<PpdFormaPgto, Long>  {
		
	public PpdFormaPgto findById(Long id) {	
		return (PpdFormaPgto)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdFormaPgto> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as formaPgto ")					
			.append("where 1=1 ");
		
		query.append("order by formaPgto.dsFormaPgto desc");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	@Override
	protected Class getPersistentClass() {		
		return PpdFormaPgto.class;
	}
	
	public void store(PpdFormaPgto formaPgto) {
		super.store(formaPgto);
	}
}	
