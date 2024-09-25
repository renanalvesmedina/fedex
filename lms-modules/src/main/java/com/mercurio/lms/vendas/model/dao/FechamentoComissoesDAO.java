package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.FechamentoComissao;

public class FechamentoComissoesDAO extends BaseCrudDao<FechamentoComissao, Long> {

	@Override
	protected Class getPersistentClass() {
		return FechamentoComissao.class;
	}
	
	public FechamentoComissao findById(Long id) {
		FechamentoComissao findById = super.findById(id);
		return findById;
	}
	
	/**
	 * Método que retorna se ja existe um fechamento para o mes atual ou nao.
	 */
	public Long findIdFechamentoMes(String tpFechamento) {
		String sql = "select id_fechamento_comissao from fechamento_comissao "
				+ "where DH_INCLUSAO between trunc(sysdate, 'MM') and last_day(sysdate) "
				+ "and tp_fechamento = :tpFechamento ";
		
		Map<String, Object> params = new TypedFlatMap();
		params.put("tpFechamento", tpFechamento);
		
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_fechamento_comissao", Hibernate.LONG);
			}
    	};

    	List<Map<String, Object>> mapResult = getAdsmHibernateTemplate().findBySqlToMappedResult(sql, params, csq);

    	if (mapResult.isEmpty()) {
    		return null;
    	}
    	
		return (Long)mapResult.iterator().next().get("id_fechamento_comissao");
	}

}
