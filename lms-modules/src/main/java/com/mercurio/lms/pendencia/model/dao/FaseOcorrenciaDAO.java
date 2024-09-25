package com.mercurio.lms.pendencia.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.pendencia.model.FaseOcorrencia;

public class FaseOcorrenciaDAO extends BaseCrudDao<FaseOcorrencia, Long>
{
	@Override
	protected Class getPersistentClass() {
		return FaseOcorrencia.class;
	}
	
	public List<FaseOcorrencia> find(FaseOcorrencia bean) {
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("faseProcesso", bean.getFaseProcesso()))
		.add(Restrictions.eq("ocorrenciaPendencia", bean.getOcorrenciaPendencia()));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	} 
	
	public ResultSetPage<FaseOcorrencia> findPaginated(PaginatedQuery paginatedQuery) {	
		StringBuilder query = new StringBuilder()
		.append("from " + getPersistentClass().getName() + " as fase_ocorrencia ")
		.append("where 1=1 ");
	
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idFaseProcesso") != null) {
			query.append("  and fase_ocorrencia.idFaseProcesso = :idFaseProcesso ");
		}
		if(MapUtils.getObject(criteria, "idFaseOcorrencia") != null) {
			query.append("  and fase_ocorrencia.idFaseOcorrencia like :idFaseOcorrencia ");
		}
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	public FaseOcorrencia findById(Long id) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("idFaseOcorrencia", id));
				
		return (FaseOcorrencia)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
}