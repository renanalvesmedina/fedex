package com.mercurio.lms.franqueados.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.RepasseFranqueado;

public class RepasseFranqueadoDAO extends BaseCrudDao<RepasseFranqueado, Long> {

	@Override
	protected Class<RepasseFranqueado> getPersistentClass() {
		return RepasseFranqueado.class;
	}
	
	/**
	 * Busca os parâmetros de repasse
	 */
	@SuppressWarnings("unchecked")
	public List<RepasseFranqueado> findRepasseFranqueado(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT rf")
		.append("  FROM " + RepasseFranqueado.class.getName() + " rf ")
		.append(" where rf.dtVigenciaInicial <= :dtIniCompetencia ")
		.append(" and rf.dtVigenciaFinal >= :dtFimCompetencia ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtIniCompetencia", dtIniCompetencia );
		params.put("dtFimCompetencia", dtFimCompetencia);
		
		return (List<RepasseFranqueado>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}

}
