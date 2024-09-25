package com.mercurio.lms.franqueados.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.DescontoFranqueado;

public class DescontoFranqueadoDAO extends BaseCrudDao<DescontoFranqueado, Long> {

	@Override
	protected Class<DescontoFranqueado> getPersistentClass() {
		return DescontoFranqueado.class;
	}

	/**
	 * Busca os descontos dos franqueados
	 */
	@SuppressWarnings("unchecked")
	public List<DescontoFranqueado> findDescontoFranqueadoByCompetencia(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT df");
		hql.append("  FROM " + DescontoFranqueado.class.getName() + " df");
		hql.append(" left join fetch df.motivoDesconto ");
		hql.append(" WHERE df.dtVigenciaInicial <= ? "); 
		hql.append("   AND df.dtVigenciaFinal >= ?"); 
		
		List<DescontoFranqueado> descontoFrqList = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{dtIniCompetencia, dtFimCompetencia});
				
		return descontoFrqList;
	}
	
}
