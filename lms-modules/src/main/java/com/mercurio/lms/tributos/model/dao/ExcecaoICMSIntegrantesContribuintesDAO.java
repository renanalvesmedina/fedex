package com.mercurio.lms.tributos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.ExcecaoICMSIntegrantesContribuintes;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ExcecaoICMSIntegrantesContribuintesDAO extends BaseCrudDao<ExcecaoICMSIntegrantesContribuintes, Long> {

	@Override	
	protected final Class getPersistentClass() {
		return ExcecaoICMSIntegrantesContribuintes.class;
	}	
	
	@SuppressWarnings("unchecked")
	public List<ExcecaoICMSIntegrantesContribuintes> findExcecaoICMSContribuintes(Long idUfOrigemAliquota, Long idUfDestino,YearMonthDay dtEmissao) {
		StringBuilder sql = new StringBuilder();
		sql.append("  from ExcecaoICMSIntegrantesContribuintes" );
		sql.append(" where 	unidadeFederativaOrigem.idUnidadeFederativa  = :idUfOrigemAliquota" );
		sql.append(" and    unidadeFederativaDestino.idUnidadeFederativa = :idUfDestino" );
		sql.append(" and    dtVigenciaInicial <= :dtEmissao " );
		sql.append(" and    dtVigenciaFinal >= :dtEmissao " );
		sql.append(" order by tpFrete desc" );
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idUfOrigemAliquota", idUfOrigemAliquota);
		params.put("idUfDestino", idUfDestino);
		params.put("dtEmissao", dtEmissao);
		return (List<ExcecaoICMSIntegrantesContribuintes>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}	
	
	public List findExcecaoICMSClienteByVigenciaEquals(
			  YearMonthDay vigenciaInicial
			, YearMonthDay vigenciaFinal
			, Long idUnidadeFederativaOrigem
			, Long idUnidadeFederativaDestino
			, String tpFrete
			, String tpIntegranteFrete
			, Long id){

		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("eic");
		
		hql.addFrom(getPersistentClass().getName() + " eic ");
		
		/** Criteria para buscar registros no mesmo intervalo de vigência */ 
		hql.addCustomCriteria("( (? between eic.dtVigenciaInicial and eic.dtVigenciaFinal) " +
							  " OR (? between eic.dtVigenciaInicial and eic.dtVigenciaFinal) " +
							  " OR (? < eic.dtVigenciaInicial  AND ? > eic.dtVigenciaFinal) )");
		
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		
		hql.addCriteria("eic.unidadeFederativaOrigem.idUnidadeFederativa", "=", idUnidadeFederativaOrigem);
		hql.addCriteria("eic.unidadeFederativaDestino.idUnidadeFederativa", "=", idUnidadeFederativaDestino);
		
		if(StringUtils.isNotBlank(tpFrete)){
			hql.addCriteria("eic.tpFrete", "=", tpFrete);
		}
		
		hql.addCriteria("eic.tpIntegranteFrete", "=", tpIntegranteFrete);
		hql.addCriteria("eic.id", "!=", id);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}	
	
	
}	
