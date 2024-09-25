package com.mercurio.lms.franqueados.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.ReembarqueFranqueado;

public class ReembarqueFranqueadoDAO extends BaseCrudDao<ReembarqueFranqueado, Long> {

	@Override
	protected Class<ReembarqueFranqueado> getPersistentClass() {
		return ReembarqueFranqueado.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReembarqueFranqueado> findReembarqueFranqueadoByDtVigenciaInicioFim(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT rf");
		hql.append("  FROM " + ReembarqueFranqueado.class.getName() + " rf");
		hql.append(" WHERE rf.dtVigenciaInicial <= :dtIniCompetencia ");
		hql.append("   AND rf.dtVigenciaFinal >= :dtFimCompetencia ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtIniCompetencia", dtIniCompetencia );
		params.put("dtFimCompetencia", dtFimCompetencia);
				
		return (List<ReembarqueFranqueado>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);	
	}
	
}
