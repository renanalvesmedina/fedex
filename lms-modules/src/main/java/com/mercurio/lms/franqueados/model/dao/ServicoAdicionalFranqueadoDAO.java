package com.mercurio.lms.franqueados.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.ServicoAdicionalFranqueado;

public class ServicoAdicionalFranqueadoDAO extends BaseCrudDao<ServicoAdicionalFranqueado, Long> {

	@Override
	protected Class getPersistentClass() {
		return ServicoAdicionalFranqueado.class;
	}
	
	public ServicoAdicionalFranqueado findByIdServicoAdicional(Long IdServicoAdicional, YearMonthDay dtInicioCompetencia) {
		StringBuilder hql = new StringBuilder("select saf from ");
		hql.append(ServicoAdicionalFranqueado.class.getName()).append(" saf ");
		hql.append(" where saf.servicoAdicional.idServicoAdicional = ?");
		hql.append(" and saf.dtVigenciaInicial <= ?	");
		hql.append(" and saf.dtVigenciaFinal >= ?	");

		ServicoAdicionalFranqueado result = 
				(ServicoAdicionalFranqueado) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[] {IdServicoAdicional, dtInicioCompetencia, dtInicioCompetencia});
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<ServicoAdicionalFranqueado> findByIdServicoAdicional(YearMonthDay dtInicioCompetencia) {
		StringBuilder hql = new StringBuilder("select saf from ");
		hql.append(ServicoAdicionalFranqueado.class.getName()).append(" saf ");
		hql.append(" where saf.dtVigenciaInicial <= ?	");
		hql.append(" and saf.dtVigenciaFinal >= ?	");

		List<ServicoAdicionalFranqueado> result = 
				(List<ServicoAdicionalFranqueado>) getAdsmHibernateTemplate().find(hql.toString(), new Object[] {dtInicioCompetencia, dtInicioCompetencia});
		
		return result;
	}
}
