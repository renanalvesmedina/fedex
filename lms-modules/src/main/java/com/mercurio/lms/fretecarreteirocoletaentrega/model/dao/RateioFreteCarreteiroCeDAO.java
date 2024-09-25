package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaRateioFreteCarreteiroCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.RateioFreteCarreteiroCE;

public class RateioFreteCarreteiroCeDAO extends BaseCrudDao<RateioFreteCarreteiroCE, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return RateioFreteCarreteiroCE.class;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("doctoServico", FetchMode.JOIN);
		fetchModes.put("pedidoColeta", FetchMode.JOIN);
		fetchModes.put("parcelaRateioFreteCarreteiroCE", FetchMode.JOIN);
	}

	public void storeAll(List<RateioFreteCarreteiroCE> list) {
		getAdsmHibernateTemplate().saveOrUpdateAll(list);
	}

	public void removeByNotaCredito(List<Long> ids) {
		StringBuilder hql = new StringBuilder();					
		hql.append("DELETE " + ParcelaRateioFreteCarreteiroCE.class.getName() + " as pr");
		hql.append(" WHERE pr.rateioFreteCarreteiroCE IN");
		hql.append(" (SELECT ra FROM "+ RateioFreteCarreteiroCE.class.getName() + " as ra  WHERE ra.notaCredito.idNotaCredito IN (:id))");
				
		getAdsmHibernateTemplate().removeByIds(hql.toString(), ids);	
		
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + RateioFreteCarreteiroCE.class.getName() + " as ra  WHERE ra.notaCredito.idNotaCredito IN (:id)", ids);				
	}
	
	public List findByRecibo(Long id) {
		StringBuilder hql = new StringBuilder();					
		
		hql.append("SELECT ra.idRateioFreteCarreteiroCE FROM "+ RateioFreteCarreteiroCE.class.getName() + " as ra  WHERE ra.notaCredito.reciboFreteCarreteiro.idReciboFreteCarreteiro =  ?");
				
		return getAdsmHibernateTemplate().find(hql.toString(), id);				
	}
}