package com.mercurio.lms.edi.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.edi.model.ClienteEDI;
import com.mercurio.lms.expedicao.model.CCEItem;

public class CCEItemDAO extends BaseCrudDao<CCEItem, Long>{

	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return CCEItem.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<CCEItem> findByCCE(String nrCce) {
		DetachedCriteria dc = createDetachedCriteria().add(Restrictions.eq("cce.idCCE", Long.valueOf(nrCce)));
						
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}

	public List<CCEItem> findByChavesNfe(List<String> chavesNotasFiscaisEncontradas) {
		DetachedCriteria dc = createDetachedCriteria().add(Restrictions.in("nrChave", chavesNotasFiscaisEncontradas))
													  .add(Restrictions.isNotNull("nrCae"));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
	public CCEItem findByChaveNfe(String nrChave) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.setFetchMode("cce", FetchMode.JOIN);
		dc.add(Restrictions.eq("nrChave", nrChave));
		//dc.add(Restrictions.isNotNull("nrCae"));
		
		List<CCEItem> retorno = getAdsmHibernateTemplate().findByCriteria(dc);
		
		return (retorno == null || retorno.isEmpty()) ? null: retorno.get(0);
	}
	
	public boolean findExistsNotasPaletizadas(List<String> chavesNotas) {
		StringBuilder query = new StringBuilder()
				.append("select count(1) as QTDE ")
				.append("from CONTROLE_CONF_ELET_ITEM ")
				.append("where NR_CHAVE in (:chavesNotas) ")
				.append(" and  BL_PALETIZADO = 'S'");
		Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("chavesNotas", chavesNotas);
        List queryResult = getAdsmHibernateTemplate().findBySql(query.toString(), parameters, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {             
                query.addScalar("QTDE", Hibernate.INTEGER);                
            }
        });
        
        Integer qtdeNotas = (Integer) queryResult.get(0);
        return qtdeNotas > 0;
	}
	

}
