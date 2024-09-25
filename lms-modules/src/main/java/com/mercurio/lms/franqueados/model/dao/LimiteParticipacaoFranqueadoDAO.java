package com.mercurio.lms.franqueados.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.LimiteParticipacaoFranqueado;

public class LimiteParticipacaoFranqueadoDAO extends BaseCrudDao<LimiteParticipacaoFranqueado, Long> {

	@Override
	protected Class getPersistentClass() {
		return LimiteParticipacaoFranqueado.class;
	}

	public List<LimiteParticipacaoFranqueado> findLimiteParticipacao(YearMonthDay dtInicioCompetencia) {
		StringBuilder sql = new StringBuilder();

		sql.append(" FROM " + LimiteParticipacaoFranqueado.class.getName()	+" lpf");
		sql.append(" WHERE lpf.dtVigenciaInicial <= ?	");
		sql.append(" 	AND lpf.dtVigenciaFinal  >= ?	");
		sql.append(" ORDER BY lpf.tpFrete				");
		sql.append(" 	,lpf.tpOperacao					");
		sql.append(" 	,lpf.nrKm						");
		
		return (List<LimiteParticipacaoFranqueado>) getAdsmHibernateTemplate().find(sql.toString(), new Object[] {dtInicioCompetencia, dtInicioCompetencia});
		
	}
}
