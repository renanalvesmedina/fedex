package com.mercurio.lms.franqueados.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;

public class ContaContabilFranqueadoDAO extends BaseCrudDao<ContaContabilFranqueado, Long> {

	@Override
	protected Class<ContaContabilFranqueado> getPersistentClass() {
		return ContaContabilFranqueado.class;
	}
	
	@Override
	public ContaContabilFranqueado findById(Long id) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as cc ");
		query.append("left outer join fetch cc.lancamentoFranqueados lf ");
		query.append("left outer  join fetch lf.pendencia pe ");
		query.append("left outer  join fetch lf.franquia fr ");
		query.append("left outer  join fetch fr.filial fil ");
		query.append("left outer  join fetch fil.pessoa p ");
		query.append("where ");
		query.append(" cc.idContaContabilFrq = :id ");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		
		return (ContaContabilFranqueado) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}


	@SuppressWarnings("unchecked")
	public List<ContaContabilFranqueado> findByVigencia(YearMonthDay vigencia) {
		StringBuffer sql = new StringBuffer()
		.append("select ccf from  ")
			.append(ContaContabilFranqueado.class.getName()).append(" as ccf ")
		.append(" where ")
		.append(" 	TRUNC(CAST(ccf.dtVigenciaInicial AS date )) <= :dtVigencia ")
		.append(" 	and TRUNC(CAST(ccf.dtVigenciaFinal AS date )) >= :dtVigencia ")
		.append("order by dsContaContabil ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtVigencia", vigencia);
		
		return (List<ContaContabilFranqueado>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}


	@SuppressWarnings("unchecked")
	public List<ContaContabilFranqueado> findContaByDtVigenciaByTipoContaByTipoLancamento(YearMonthDay dtVigencia, String tipoConta, String tipoLancamento) {
		StringBuffer sql = new StringBuffer()
		.append("select ccf from  ")
			.append(ContaContabilFranqueado.class.getName()).append(" as ccf 	")
		.append(" where 														")
			.append(" ccf.tpContaContabil = :tpContaContabil 					")
			.append(" and ccf.tpLancamento = :tpLancamento 					")
			.append(" 	and dtVigenciaInicial <= :dtVigencia 			")
			.append(" 	and dtVigenciaFinal >= :dtVigencia 				");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtVigencia", dtVigencia == null? new YearMonthDay() : dtVigencia);
		params.put("tpContaContabil", tipoConta);
		params.put("tpLancamento", tipoLancamento);
		
		
		return (List<ContaContabilFranqueado>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}

}
