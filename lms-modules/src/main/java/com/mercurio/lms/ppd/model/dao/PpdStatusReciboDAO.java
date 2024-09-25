package com.mercurio.lms.ppd.model.dao;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;

public class PpdStatusReciboDAO extends BaseCrudDao<PpdStatusRecibo, Long>  {
		
	public PpdStatusRecibo findById(Long id) {	
		return (PpdStatusRecibo)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdStatusRecibo> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as status ")
			.append("	left join fetch status.recibo as recibo ")
			.append("	left join fetch status.usuario as usuario ")
			.append("	left join fetch status.usuario.usuarioADSM as usuarioADSM ")
			.append("where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idRecibo") != null) {
			query.append("  and status.recibo.idRecibo = :idRecibo ");
		}
		query.append("order by status.dhStatusRecibo.value desc");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	
	@Override
	protected Class getPersistentClass() {		
		return PpdStatusRecibo.class;
	}
	
	public void store(PpdStatusRecibo statusRecibo) {
		super.store(statusRecibo);
	}
}	
