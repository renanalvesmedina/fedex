package com.mercurio.lms.ppd.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.ppd.model.PpdLoteJde;

public class PpdLoteJdeDAO extends BaseCrudDao<PpdLoteJde, Long>  {
		
	public PpdLoteJde findById(Long id) {	
		return (PpdLoteJde)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario", FetchMode.JOIN);		
		lazyFindById.put("usuario.usuarioADSM", FetchMode.JOIN);		
	}	
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdLoteJde> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as lote ")			
			.append("where 1=1 ");
		
		query.append("order by lote.idLoteJde desc");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
		
	public PpdLoteJde findLoteAberto() {			
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setFetchMode("usuario", FetchMode.JOIN);
		dc.setFetchMode("usuario.usuarioADSM", FetchMode.JOIN);
		dc.add(Restrictions.isNull("dhEnvio"));		
		return (PpdLoteJde)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public PpdLoteJde findUltimoLoteEnviado() {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setFetchMode("usuario", FetchMode.JOIN);
		dc.setFetchMode("usuario.usuarioADSM", FetchMode.JOIN);
		dc.add(Restrictions.isNotNull("dhEnvio"));
		dc.addOrder(Order.desc("dhEnvio"));
		List<PpdLoteJde> lotesEnviados = getAdsmHibernateTemplate().findByCriteria(dc);
		if(lotesEnviados != null && lotesEnviados.size() > 0) {
			return lotesEnviados.get(0);
		} else {			
			return null;
		}
	}
	
	@Override
	protected Class getPersistentClass() {		
		return PpdLoteJde.class;
	}
	
	public void store(PpdLoteJde loteJde) {
		super.store(loteJde);
	}
}	
