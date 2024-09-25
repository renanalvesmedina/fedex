package com.mercurio.lms.ppd.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.dao.OneObjectExpectedException;
import com.mercurio.lms.ppd.model.PpdLoteRecibo;

public class PpdLoteReciboDAO extends BaseCrudDao<PpdLoteRecibo, Long>  {
		
	public PpdLoteRecibo findById(Long id) {	
		return (PpdLoteRecibo)super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<PpdLoteRecibo> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as loteRecibo ")	
			.append("	left join fetch loteRecibo.recibo as recibo ")
			.append("	left join fetch loteRecibo.lote as loteJde ")
			.append("	left join fetch recibo.naturezaProduto as natureza ")
			.append("	left join fetch recibo.filial as filial ")
			.append("	left join fetch recibo.pessoa as pessoa ")
			.append("	left join fetch recibo.formaPgto as formaPgto ")
			.append("	left join fetch loteRecibo.lote.usuario as usuario ")			
			.append("	left join fetch loteRecibo.lote.usuario.usuarioADSM as usuarioADSM ")
			.append("	left join fetch loteRecibo.recibo.filial.pessoa as filialRecibo ")
			.append("where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(criteria.get("idLoteJde") != null) {
			query.append(" and loteRecibo.lote.idLoteJde = :idLoteJde");		
		}		
		if(criteria.get("blAberto") != null) {
			if((Boolean)criteria.get("blAberto")) {
				query.append(" and loteRecibo.lote.dhEnvio is null");
			} else {
				query.append(" and loteRecibo.lote.dhEnvio not is null");
			}
		}	
		query.append(" order by recibo.nrRecibo desc");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}	
	
	public PpdLoteRecibo findReciboInLoteNaoEnviado(Long idRecibo) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setFetchMode("lote", FetchMode.JOIN);
		dc.createAlias("lote", "lote");
		
		dc.add(Restrictions.eq("recibo.idRecibo", idRecibo));				
		dc.add(Restrictions.isNull("lote.dhEnvio"));
		
		List queryResult = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if(queryResult != null) {
			if (queryResult.size() > 1) {
				throw new OneObjectExpectedException();
			}
			else if (queryResult.size() == 1) {
				return (PpdLoteRecibo) queryResult.get(0);	
			}
		}
		
		return null;
	}
	
	public List<PpdLoteRecibo> findByIdLoteJde(Long idLoteJde) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setFetchMode("recibo", FetchMode.JOIN);
		dc.setFetchMode("lote", FetchMode.JOIN);
		dc.add(Restrictions.eq("lote.idLoteJde", idLoteJde));		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);		
	}
	
	@Override
	protected Class getPersistentClass() {		
		return PpdLoteRecibo.class;
	}
	
	public void store(PpdLoteRecibo loteJde) {
		super.store(loteJde);
	}
}	
