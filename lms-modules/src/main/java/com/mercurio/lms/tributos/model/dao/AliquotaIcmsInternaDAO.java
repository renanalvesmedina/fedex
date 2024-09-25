package com.mercurio.lms.tributos.model.dao;

import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaIcmsInterna;

public class AliquotaIcmsInternaDAO  extends BaseCrudDao<AliquotaIcmsInterna, Long> {

	@Override
	protected Class getPersistentClass() {
		return AliquotaIcmsInterna.class;
	}

	public AliquotaIcmsInterna findByIdUnidadeFederativa(Long idUnidadeFederativa, YearMonthDay dtAtual) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT aii ")
		.append("FROM ").append(AliquotaIcmsInterna.class.getSimpleName()).append(" aii ")
		.append("JOIN ").append(" aii.unidadeFederativa uf ")
		.append("WHERE ")
		.append("	 uf.idUnidadeFederativa =:idUnidadeFederativa ")
		.append("AND aii.dtVigenciaInicial <=:dtAtual ")
		.append("AND (aii.dtVigenciaFinal IS NULL OR  aii.dtVigenciaFinal >=:dtAtual) ")
		;
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idUnidadeFederativa", idUnidadeFederativa);
		parameters.put("dtAtual", dtAtual);
		
		List<AliquotaIcmsInterna> l = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parameters);
		
		if (!CollectionUtils.isEmpty(l)) {
			return l.get(0);
		}
		
		return null;
	}

}
