package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ObservacaoMercadoria;

public class ObservacaoMercadoriaDAO extends BaseCrudDao<ObservacaoMercadoria, Long> {
	
	@Override
	protected final Class getPersistentClass() {
		return ObservacaoMercadoria.class;
	}

	private List<Criterion> createCriterions(Long idDoctoServico) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		if (idDoctoServico != null) {
			criterions.add(Restrictions.eq("doctoServico.idDoctoServico", idDoctoServico));
		}

		return criterions;
	}
	
	public String findSgFilialFromIdUsuario(Long idUsuario) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sg_filial from usuario u, v_funcionario f ");
		sb.append("  where u.nr_matricula = f.nr_matricula ");
		sb.append("    and u.id_usuario = :idUsuario ");

		Map<String, Object> params = new TypedFlatMap();		
		params.put("idUsuario", idUsuario); 

    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
			}
    	};

    	List<Map<String, Object>> mapResult = getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), params, csq);
    	if (mapResult.isEmpty()) {
    		return "";
    	}
    	
    	return mapResult.iterator().next().get("sg_filial").toString();
	}
	
	public List<ObservacaoMercadoria> findByDoctoServico(Long idDoctoServico) {
		Criteria criteria = createCriteria();
		
		List<Criterion> criterionList = createCriterions(idDoctoServico);
		if (criterionList != null) {
	    	for (Criterion criterion : criterionList) {
	    		criteria.add(criterion);
			}
		}
		criteria.addOrder(Order.desc("dhInclusao"));

		return criteria.list();
	}
	
}
