package com.mercurio.lms.franqueados.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.DistanciaTransferenciaFranqueado;

public class DistanciaTransferenciaFranqueadoDAO extends BaseCrudDao<DistanciaTransferenciaFranqueado, Long> {

	@Override
	protected Class<DistanciaTransferenciaFranqueado> getPersistentClass() {
		return DistanciaTransferenciaFranqueado.class;
	}
	
	public List<DistanciaTransferenciaFranqueado> findDistanciaTransferenciaFranqueado(YearMonthDay dtIniCompetencia){
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT dt							");
		hql.append("  FROM " + DistanciaTransferenciaFranqueado.class.getName() + " dt");
		hql.append(" WHERE dt.dtVigenciaInicial <= ? 	"); 
		hql.append("	AND dt.dtVigenciaFinal >= ?		");
		hql.append(" ORDER BY							");
		hql.append(" 	dt.tpFrete asc,					");
		hql.append(" 	dt.nrKm asc					");
		
		List<DistanciaTransferenciaFranqueado> distanciaTransferenciaFranqueados = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{dtIniCompetencia, dtIniCompetencia});
				
		return distanciaTransferenciaFranqueados;
	}

}
