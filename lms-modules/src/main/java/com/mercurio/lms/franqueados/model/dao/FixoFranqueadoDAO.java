package com.mercurio.lms.franqueados.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.FixoFranqueado;

public class FixoFranqueadoDAO extends BaseCrudDao<FixoFranqueado, Long> {

	@Override
	protected Class<FixoFranqueado> getPersistentClass() {
		return FixoFranqueado.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<FixoFranqueado> findParametrosFranquiaPeriodo(long idFranquia, YearMonthDay dtInicioCompetencia){  
		StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();

		sql.append("SELECT fxf ");
		sql.append(" FROM " + FixoFranqueado.class.getName() + " fxf ");
		sql.append(" WHERE fxf.franquia.idFranquia = ?");
		sql.append("   and fxf.dtVigenciaInicial <= ?");
		sql.append("   and fxf.dtVigenciaFinal >= ?");
		sql.append("   and fxf.municipio is null");
		sql.append("   and fxf.cliente is null ");

		param.add(idFranquia);
		param.add(dtInicioCompetencia);
		param.add(dtInicioCompetencia);

		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<FixoFranqueado> findParametrosFranquia(Long idFranquia) {
		String query = new StringBuilder()
		.append(" SELECT fxf ")
		.append(" FROM " + FixoFranqueado.class.getName() + " fxf ")
		.append(" WHERE fxf.franquia.idFranquia = ?").toString();

		return getAdsmHibernateTemplate().find(query, new Object[]{idFranquia});
	}
	
}
