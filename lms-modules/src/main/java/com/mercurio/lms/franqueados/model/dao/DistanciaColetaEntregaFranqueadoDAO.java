package com.mercurio.lms.franqueados.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.DistanciaColetaEntregaFranqueado;

public class DistanciaColetaEntregaFranqueadoDAO extends BaseCrudDao<DistanciaColetaEntregaFranqueado, Long> {

	@Override
	protected Class<DistanciaColetaEntregaFranqueado> getPersistentClass() {
		return DistanciaColetaEntregaFranqueado.class;
	}

	
	@SuppressWarnings("unchecked")
	public List<DistanciaColetaEntregaFranqueado> findDistanciaColetaEntregaFranqueado(YearMonthDay dtIniCompetencia) {
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT dce							");
		hql.append("  FROM " + DistanciaColetaEntregaFranqueado.class.getName() + " dce");
		hql.append(" WHERE dce.dtVigenciaInicial <= ? 	"); 
		hql.append("	AND dce.dtVigenciaFinal >= ?	");
		hql.append(" ORDER BY							");
		hql.append(" 	dce.tpPavimento asc,				");
		hql.append(" 	dce.nrKm asc					");
		
		List<DistanciaColetaEntregaFranqueado> distanciaColetaEntregaFranqueados = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{dtIniCompetencia, dtIniCompetencia});
				
		return distanciaColetaEntregaFranqueados;
	}

}
