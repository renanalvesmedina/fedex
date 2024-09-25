package com.mercurio.lms.franqueados.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.FreteLocalFranqueado;

public class FreteLocalFranqueadoDAO extends BaseCrudDao<FreteLocalFranqueado, Long> {

	@Override
	protected Class<FreteLocalFranqueado> getPersistentClass() {
		return FreteLocalFranqueado.class;
	}
	
	/**
	 * Busca os parâmetros do frete local
	 */
	@SuppressWarnings("unchecked")
	public List<FreteLocalFranqueado> findByCompetencia(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT fl");
		hql.append("  FROM " + FreteLocalFranqueado.class.getName() + " fl");
		hql.append(" WHERE fl.dtVigenciaInicial <= ? "); 
		hql.append("   AND fl.dtVigenciaFinal >= ?"); 
		
		List<FreteLocalFranqueado> freteLocalList = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{dtIniCompetencia, dtFimCompetencia});
		
		return freteLocalList;
	}

}
